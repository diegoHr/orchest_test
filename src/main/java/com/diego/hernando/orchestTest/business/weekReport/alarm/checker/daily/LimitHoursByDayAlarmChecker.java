package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.daily;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.weekReport.service.HoursWorkedCalculatorService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LimitHoursByDayAlarmChecker implements IDailyAlarmCheckerService {

    private final HoursWorkedCalculatorService hoursWorkedCalcSrv;
    protected final List<Integer> limitHoursOfWeek = Arrays.asList(10,10,10,10,10,10,10);


    @Autowired
    public LimitHoursByDayAlarmChecker(HoursWorkedCalculatorService hoursWorkedCalcSrv) {
        this.hoursWorkedCalcSrv = hoursWorkedCalcSrv;
    }

    @Override
    public String getKeyDescription() {
        return "alarm.checker.limit.hours.day";
    }

    @Override
    public List<Alarm> check(List<WorkSignDto> workSignsToCheck) {
        if(workSignsToCheck.size() > 0) {
            DateTime day = new DateTime(workSignsToCheck.get(0).getDate());
            int limitHoursOfDay = limitHoursOfWeek.get(day.getDayOfWeek());
            if(limitHoursOfDay < hoursWorkedCalcSrv.calculateHoursWorked(workSignsToCheck)){
                return Collections.singletonList(new Alarm(workSignsToCheck, getKeyDescription(), new Object[]{limitHoursOfDay, day}, getLevel()));
            }
        }
        return new ArrayList<>();
    }

    @Override
    public AlarmLevel getLevel() {
        return AlarmLevel.WARNING;
    }
}
