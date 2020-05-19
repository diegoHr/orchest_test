package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.weekly;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.helper.IncompleteWSignsOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.AlarmParameterFormattersFactoryService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.IAlarmParameterFormatter;
import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.IntegerAlarmParameterFormatter;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IncompleteWorkWSignAlarmCheckerService implements IWeeklyAlarmCheckerService {

    private final WorkSignOperationsService workSignOpSrv;
    private final IncompleteWSignsOperationsService incompleteWSignOpSrv;
    private final AlarmParameterFormattersFactoryService alarmParameterFactory;

    @Autowired
    public IncompleteWorkWSignAlarmCheckerService(WorkSignOperationsService workSignOpSrv,
                                                  IncompleteWSignsOperationsService incompleteWSignOpSrv,
                                                  AlarmParameterFormattersFactoryService alarmParameterFactory) {
        this.workSignOpSrv = workSignOpSrv;
        this.incompleteWSignOpSrv = incompleteWSignOpSrv;
        this.alarmParameterFactory = alarmParameterFactory;
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

        return wSignsTrigeredAlarm.size() > 0 ?
                Collections.singletonList(new Alarm(wSignsTrigeredAlarm, getKeyDescription(),
                        new Object[]{wSignsTrigeredAlarm.size()}, getParameterFormatters(), getLevel()))
                :new ArrayList<>();
    }

    @Override
    public AlarmLevel getLevel() {
        return AlarmLevel.ERROR;
    }

    @Override
    public List<IAlarmParameterFormatter<Object, Object>> getParameterFormatters() {
        return Collections.singletonList(alarmParameterFactory.getAlarmParameterFormatter(IntegerAlarmParameterFormatter.class));
    }


}
