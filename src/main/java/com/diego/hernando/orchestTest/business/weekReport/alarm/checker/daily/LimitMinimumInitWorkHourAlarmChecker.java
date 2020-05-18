package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.daily;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import com.diego.hernando.orchestTest.configuration.Constants;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LimitMinimumInitWorkHourAlarmChecker implements IDailyAlarmCheckerService {

    private final WorkSignOperationsService wSignOpSrv;

    private final DateOperationsService dateOpsSrv;

    private final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(Constants.PATTERN_SIMPLE_HOUR_JODA);
    protected final List<DateTime> limitMinInitHourByDayWeek = Arrays.asList(
            dateFormatter.parseDateTime("08:00"),
            dateFormatter.parseDateTime("08:00"),
            dateFormatter.parseDateTime("08:00"),
            dateFormatter.parseDateTime("08:00"),
            dateFormatter.parseDateTime("07:00"),
            null,
            null);

    @Autowired
    public LimitMinimumInitWorkHourAlarmChecker(WorkSignOperationsService wSignOpSrv, DateOperationsService dateOpsSrv) {
        this.wSignOpSrv = wSignOpSrv;
        this.dateOpsSrv = dateOpsSrv;
    }

    @Override
    public String getKeyDescription() {
        return "alarm.checker.limit.minimum_init_work_hour";
    }

    @Override
    public AlarmLevel getLevel() {
        return AlarmLevel.WARNING;
    }

    @Override
    public List<Alarm> check(List<WorkSignDto> workSignsToCheck) {
        if(workSignsToCheck.size() == 0){
            return new ArrayList<>();
        }
        DateTime minHourInit = getMinHourInit(new DateTime(workSignsToCheck.get(0).getDate()));

        WorkSignDto initWsign = findInitWSign(minHourInit, workSignsToCheck);
        DateTime initWorkDay = new DateTime(
                wSignOpSrv.isInRecordTypeAndWorkTypeWSign(initWsign)
                        ? initWsign.getDate()
                        : dateOpsSrv.getInitDayFromDate(initWsign.getDate())
        );

        return isInitWorkDayValid(minHourInit, initWorkDay)
                ? new ArrayList<>()
                : Collections.singletonList(
                        new Alarm(Collections.singletonList(initWsign),
                                getKeyDescription(),
                                new Object[]{minHourInit.toDate(), minHourInit.toDate()},
                                getLevel()
                        )
        );
    }

    private WorkSignDto findInitWSign(DateTime minHourInit, List<WorkSignDto> workSignsToCheck){
        return workSignsToCheck.stream()
                .filter(wSign ->
                        wSignOpSrv.isInRecordTypeAndWorkTypeWSign(wSign)
                                || isInitWorkDayValid(minHourInit, new DateTime(wSign.getDate()))
                ).findFirst()
                .orElse(workSignsToCheck.get(0));
    }

    protected DateTime getMinHourInit (DateTime day){
        return limitMinInitHourByDayWeek.get(day.getDayOfWeek()-1);
    }

    private boolean isInitWorkDayValid(DateTime minHourInit, DateTime initWorkDay){

        return minHourInit == null || minHourInit.getHourOfDay() < initWorkDay.getHourOfDay()
                || minHourInit.getHourOfDay() == initWorkDay.getHourOfDay()
                && minHourInit.getMinuteOfHour() <= initWorkDay.getMinuteOfHour();
    }



}
