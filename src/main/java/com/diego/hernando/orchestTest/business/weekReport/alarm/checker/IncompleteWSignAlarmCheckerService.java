package com.diego.hernando.orchestTest.business.weekReport.alarm.checker;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IncompleteWSignAlarmCheckerService implements IWeeklyAlarmCheckerWeeklyService {

    private WorkSignOperationsService workSignOpSrv;

    @Autowired
    public IncompleteWSignAlarmCheckerService(WorkSignOperationsService workSignOpSrv) {
        this.workSignOpSrv = workSignOpSrv;
    }

    @Override
    public String getKeyDescription() {
        return "alarm.checker.incomplete.worksign";
    }

    @Override
    public List<Alarm> check(List<WorkSignDto> workSignsToCheck) {

        List<WorkSignDto> workSignsOfWork = workSignsToCheck.stream()
                .filter(workSignOpSrv::isWorkTypeWorkSign).collect(Collectors.toList());
        List<WorkSignDto> wSignsTrigeredAlarm = extractIncompleteWSigns(workSignsOfWork);

        return Arrays.asList(new Alarm(wSignsTrigeredAlarm,getKeyDescription(),new Object[]{wSignsTrigeredAlarm.size()}, getLevel()));
    }

    @Override
    public AlarmLevel getLevel() {
        return AlarmLevel.ERROR;
    }

    protected List<WorkSignDto> extractIncompleteWSigns(List<WorkSignDto> workSignDtos){
        List<WorkSignDto> incompleteWSigns = new ArrayList<>();
        getFirstWSignIfIsIncomplete(workSignDtos).ifPresent(incompleteWSigns::add);
        for(int i = 0; i < workSignDtos.size()-1; i++){
            WorkSignDto previousWorkSign = workSignDtos.get(i);
            WorkSignDto nextWorkSign = workSignDtos.get(i+1);

            getIncompleteWSign(previousWorkSign, nextWorkSign).ifPresent(incompleteWSigns::add);
        }
        getLastWSignIfIsIncomplete(workSignDtos).ifPresent(incompleteWSigns::add);
        return incompleteWSigns;
    }

    protected Optional<WorkSignDto> getFirstWSignIfIsIncomplete (List<WorkSignDto> workSignDtos){
        return workSignDtos.size()>0 && workSignOpSrv.isOutRecordTypeWSign(workSignDtos.get(0))
                ? Optional.of(workSignDtos.get(0))
                : Optional.empty();
    }

    protected Optional<WorkSignDto> getLastWSignIfIsIncomplete (List<WorkSignDto> workSignDtos){
        return workSignDtos.size()>0 && workSignOpSrv.isInRecordTypeWSign(workSignDtos.get(workSignDtos.size()-1))
                ? Optional.of(workSignDtos.get(workSignDtos.size()-1))
                : Optional.empty();
    }

    protected Optional<WorkSignDto> getIncompleteWSign (WorkSignDto wSign, WorkSignDto nextWSign){
        if(workSignOpSrv.isInRecordTypeWSign(wSign) && workSignOpSrv.isInRecordTypeWSign(nextWSign)){
            return Optional.of(wSign);
        }else if (workSignOpSrv.isOutRecordTypeWSign(wSign) && workSignOpSrv.isOutRecordTypeWSign(nextWSign)){
            return Optional.of(nextWSign);
        }else {
            return Optional.empty();
        }
    }

}
