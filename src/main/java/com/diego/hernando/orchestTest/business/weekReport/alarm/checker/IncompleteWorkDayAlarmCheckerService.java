package com.diego.hernando.orchestTest.business.weekReport.alarm.checker;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IncompleteWorkDayAlarmCheckerService implements IAlarmCheckerService {

    @Autowired
    private WorkSignOperationsService workSignOpSrv;

    @Override
    public String getKeyDescription() {
        return "alarm.checker.incomplete.work.day";
    }

    @Override
    public List<Alarm> check(List<WorkSignDto> workSignsToCheck,
                                       List<WorkSignDto> previousWorkSigns,
                                       List<WorkSignDto> laterWorkSigns) {

        List<WorkSignDto> workSignsOfWork = workSignsToCheck.stream()
                .filter(workSignOpSrv.getPredicateIsWorkTypeWorkSign()).collect(Collectors.toList());

        WorkSignDto lastPreviousDayWorkSign = previousWorkSigns.stream().filter(
                workSignOpSrv.getPredicateIsWorkTypeWorkSign()).reduce((first,second)-> second).orElse(null);

        WorkSignDto firstNextDayWorkSignsOfWork = laterWorkSigns.stream()
                .filter(workSignOpSrv.getPredicateIsWorkTypeWorkSign()).findFirst().orElse(null);

        return extractIncompleteWSignAndPackageInAlarms(workSignsOfWork, lastPreviousDayWorkSign, firstNextDayWorkSignsOfWork);
    }

    protected List<Alarm> extractIncompleteWSignAndPackageInAlarms(List<WorkSignDto> workSignDtos,
                                                                 WorkSignDto lastPreviousDayWorkSign,
                                                                 WorkSignDto firstLaterDayWorkSignsOfWork){
        List<Alarm> alarms = new ArrayList<>();
        for(int i = 0; i < workSignDtos.size()-1; i++){
            WorkSignDto previousWorkSign = workSignDtos.get(i);
            WorkSignDto nextWorkSign = workSignDtos.get(i+1);

            if(i == 0){
                optionalAlarmIncompleteFirstWSign(previousWorkSign, lastPreviousDayWorkSign).ifPresent(alarms::add);
            }else if(i >= workSignDtos.size() - 2){
                optionalAlarmIncompleteLastWSign(nextWorkSign, firstLaterDayWorkSignsOfWork).ifPresent(alarms::add);
            }else{
                optionalAlarmIncompleteWSign(previousWorkSign, nextWorkSign).ifPresent(alarms::add);
            }
        }
        return alarms;
    }

    private Optional<Alarm> optionalAlarmIncompleteLastWSign(WorkSignDto lastWorkSign, WorkSignDto firstLaterDayWorkSignsOfWork) {
        if(!(firstLaterDayWorkSignsOfWork != null
                && checkIfLastWSignIsInRTypeAndIsComplete(lastWorkSign, firstLaterDayWorkSignsOfWork))){
            return Optional.of(new Alarm(Arrays.asList(lastWorkSign, firstLaterDayWorkSignsOfWork),getKeyDescription(), null));
        }
        return Optional.empty();
    }

    protected Optional<Alarm> optionalAlarmIncompleteFirstWSign(WorkSignDto workSignDto, WorkSignDto lastPreviousDayWorkSign){
        if(!(lastPreviousDayWorkSign != null
                && checkIfFirstWSignIsOutRTypeAndIsComplete(workSignDto, lastPreviousDayWorkSign)
        )){
            return Optional.of(new Alarm(Arrays.asList(lastPreviousDayWorkSign,workSignDto), getKeyDescription(), null));
        }
        return Optional.empty();
    }

    protected Optional<Alarm> optionalAlarmIncompleteWSign (WorkSignDto previousWorkSign, WorkSignDto nextWorkSign){
        if(checkIfPreviousWSignIsOutNextWSignIsIn(previousWorkSign,nextWorkSign)
        || checkIfPreviousWSignIsInNextWSignIsOut(previousWorkSign, nextWorkSign)){
            return Optional.empty();
        }
        return Optional.of(new Alarm(Arrays.asList(previousWorkSign, nextWorkSign), getKeyDescription(), null));
    }

    private boolean checkIfLastWSignIsInRTypeAndIsComplete(WorkSignDto lastWorkSign, WorkSignDto firstLaterDayWorkSignsOfWork) {
        return workSignOpSrv.isInRecordTypeWorkSign(lastWorkSign) &&  workSignOpSrv.isOutRecordTypeWorkSign(firstLaterDayWorkSignsOfWork);
    }

    private boolean checkIfFirstWSignIsOutRTypeAndIsComplete(WorkSignDto firstWorkSignOfDay, WorkSignDto yesterdayLastWorkSign){
        return workSignOpSrv.isOutRecordTypeWorkSign(firstWorkSignOfDay) &&  workSignOpSrv.isInRecordTypeWorkSign(yesterdayLastWorkSign);
    }
    private boolean checkIfPreviousWSignIsOutNextWSignIsIn(WorkSignDto previousWorkSign, WorkSignDto nextWorkSign){
        return workSignOpSrv.isOutRecordTypeWorkSign(previousWorkSign) && workSignOpSrv.isInRecordTypeWorkSign(nextWorkSign);
    }

    private boolean checkIfPreviousWSignIsInNextWSignIsOut(WorkSignDto previousWorkSign, WorkSignDto nextWorkSign){
        return workSignOpSrv.isInRecordTypeWorkSign(previousWorkSign) && workSignOpSrv.isOutRecordTypeWorkSign(nextWorkSign);
    }
}
