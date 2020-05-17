package com.diego.hernando.orchestTest.business.weekReport.alarm.checker;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.helper.IncompleteWSignsOperationsService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IncompleteWorkWSignAlarmCheckerService implements IWeeklyAlarmCheckerWeeklyService {

    private final WorkSignOperationsService workSignOpSrv;
    private final IncompleteWSignsOperationsService incompleteWSignOpSrv;

    @Autowired
    public IncompleteWorkWSignAlarmCheckerService(WorkSignOperationsService workSignOpSrv, IncompleteWSignsOperationsService incompleteWSignOpSrv) {
        this.workSignOpSrv = workSignOpSrv;
        this.incompleteWSignOpSrv = incompleteWSignOpSrv;
    }


    @Override
    public String getKeyDescription() {
        return "alarm.checker.incomplete.work.worksign";
    }

    @Override
    public List<Alarm> check(List<WorkSignDto> workSignsToCheck) {

        List<WorkSignDto> workSignsOfWork = workSignsToCheck.stream()
                .filter(workSignOpSrv::isWorkTypeWorkSign).collect(Collectors.toList());
        List<WorkSignDto> wSignsTrigeredAlarm = incompleteWSignOpSrv.extractIncompleteWSigns(workSignsOfWork);

        return Arrays.asList(new Alarm(wSignsTrigeredAlarm,getKeyDescription(),new Object[]{wSignsTrigeredAlarm.size()}, getLevel()));
    }

    @Override
    public AlarmLevel getLevel() {
        return AlarmLevel.ERROR;
    }





}
