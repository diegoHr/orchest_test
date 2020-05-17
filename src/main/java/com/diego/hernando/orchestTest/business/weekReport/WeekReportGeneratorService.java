package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmDto;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmManagerService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignReturnerByWeeksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Service
public class WeekReportGeneratorService {

    @Autowired
    private WorkSignReturnerByWeeksService workSignRetByWeeksSrv;

    @Autowired
    private HoursWorkedCalculatorService hoursWorkedCalculatorSrv;

    @Autowired
    private AlarmManagerService alarmMngrSrv;


    public WeekReportDto getWeekReport (int week, String businessId, String employeeId, Locale localeUser){

        List<WorkSignDto> weekWSigns = workSignRetByWeeksSrv.getEmployeeWSignsOfWeek(businessId, employeeId, week);
        List<AlarmDto> weekAlarms = alarmMngrSrv.generateWeeklyAlarms(weekWSigns, localeUser);

        Double hour = hoursWorkedCalculatorSrv.calculateHoursWorked(getWSignsWithoutErrors(weekWSigns, weekAlarms));


        return null;
    }

    protected List<WorkSignDto> getWSignsWithoutErrors (List<WorkSignDto> wSigns, List<AlarmDto> alarms){
        List<WorkSignDto> errorWSigns = alarmMngrSrv.getWorkSignsThatTriggeredErrorAlarms(alarms);

        return wSigns.stream()
                .filter(wSign -> !errorWSigns.contains(wSign)).collect(Collectors.toList());
    }

}
