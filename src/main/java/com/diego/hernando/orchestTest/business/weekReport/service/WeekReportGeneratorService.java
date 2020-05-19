package com.diego.hernando.orchestTest.business.weekReport.service;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.DayReportDto;
import com.diego.hernando.orchestTest.business.weekReport.WeekReportDto;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmDto;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmManagerService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignReturnerByWeeksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WeekReportGeneratorService {

    @Autowired
    private WorkSignReturnerByWeeksService workSignRetByWeeksSrv;

    @Autowired
    private HoursWorkedCalculatorService hoursWorkedCalculatorSrv;

    @Autowired
    private AlarmManagerService alarmMngrSrv;

    @Autowired
    private DateOperationsService dateOpSrv;

    @Autowired
    private DayReportManagerService dayReportMgrSrv;

    public List<WeekReportDto> getWeekReports (int month, int year, String businessId, String employeeId, Locale localeUser){
        List<Date> initWeeks = dateOpSrv.getInitWeeks(month, year);
        return initWeeks.stream().map(initWeek -> getWeekReport(initWeek, businessId, employeeId, localeUser))
                .collect(Collectors.toList());
    }

    public WeekReportDto getWeekReport (int week, String businessId, String employeeId, Locale localeUser){
        Date initWeekDate = dateOpSrv.transformWeekToInitWeekDate(week);
        return getWeekReport(initWeekDate, businessId, employeeId, localeUser);
    }

    public WeekReportDto getWeekReport (Date initWeekDate, String businessId, String employeeId, Locale localeUser){
        List<WorkSignDto> weekWSigns = workSignRetByWeeksSrv.getEmployeeWSingsOfWeek(businessId, employeeId, initWeekDate);
        List<AlarmDto> weekAlarms = alarmMngrSrv.generateWeeklyAlarms(weekWSigns, localeUser);

        Double hoursWorked = hoursWorkedCalculatorSrv.calculateHoursWorked(getWSignsWithoutErrors(weekWSigns, weekAlarms));
        List<DayReportDto> dayReports = dayReportMgrSrv.getDayReportsFromWSignsOfWeek(weekWSigns);

        List<AlarmDto> dayAlarms = getDayAlarms(dayReports, localeUser,
                alarmMngrSrv.getWorkSignsThatTriggeredErrorAlarms(weekAlarms));

        return new WeekReportDto(initWeekDate, hoursWorked, dayReports,
                Stream.concat(
                        weekAlarms.stream(),
                        dayAlarms.stream()).collect(Collectors.toList())
        );
    }



    protected List<AlarmDto> getDayAlarms (List<DayReportDto> dayReports, Locale locale,
                                           List<WorkSignDto> wSignsThatTriggeredErrorAlarms ){
        return dayReports.stream()
                .map(DayReportDto::getDayWorkSigns)
                .map(wSigns -> alarmMngrSrv.generateDailyAlarms(
                        removeWSingsNotAllowed(wSigns, wSignsThatTriggeredErrorAlarms), locale))
                .flatMap(List::stream).collect(Collectors.toList());
    }

    protected List<WorkSignDto> removeWSingsNotAllowed (List<WorkSignDto> wSigns, List<WorkSignDto> wSignsNotAllowed ){
        return wSigns.stream()
                .filter(wSign -> !wSignsNotAllowed.contains(wSign)).collect(Collectors.toList());
    }

    protected List<WorkSignDto> getWSignsWithoutErrors (List<WorkSignDto> wSigns, List<AlarmDto> alarms){
        List<WorkSignDto> errorWSigns = alarmMngrSrv.getWorkSignsThatTriggeredErrorAlarms(alarms);
        return removeWSingsNotAllowed(wSigns, errorWSigns);
    }

}
