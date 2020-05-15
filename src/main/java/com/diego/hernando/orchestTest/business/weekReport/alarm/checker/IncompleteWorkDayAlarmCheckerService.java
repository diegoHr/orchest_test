package com.diego.hernando.orchestTest.business.weekReport.alarm.checker;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IncompleteWorkDayAlarmCheckerService implements IAlarmCheckerService {

    private WorkSignOperationsService workSignOpSrv;

    @Autowired
    public IncompleteWorkDayAlarmCheckerService(WorkSignOperationsService workSignOpSrv) {
        this.workSignOpSrv = workSignOpSrv;
    }

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
            }
            if(i >= workSignDtos.size() - 2){
                optionalAlarmIncompleteLastWSign(nextWorkSign, firstLaterDayWorkSignsOfWork).ifPresent(alarms::add);
            }

            optionalAlarmIncompleteWSign(previousWorkSign, nextWorkSign).ifPresent(alarms::add);
        }
        return alarms;
    }

    protected Optional<Alarm> optionalAlarmIncompleteLastWSign(WorkSignDto lastWorkSign, WorkSignDto firstLaterDayWorkSignsOfWork) {
        if(!(firstLaterDayWorkSignsOfWork != null
                && checkIfLastWSignIsInRTypeAndIsComplete(lastWorkSign, firstLaterDayWorkSignsOfWork)
                || workSignOpSrv.isOutRecordTypeWSign(lastWorkSign))){
            return Optional.of(new Alarm(
                    Stream.of(lastWorkSign, firstLaterDayWorkSignsOfWork).filter(Objects::nonNull)
                            .collect(Collectors.toList()),
                    getKeyDescription(),
                    null)
            );
        }
        return Optional.empty();
    }

    protected Optional<Alarm> optionalAlarmIncompleteFirstWSign(WorkSignDto workSignDto, WorkSignDto lastPreviousDayWorkSign){
        if(!(lastPreviousDayWorkSign != null
                && checkIfFirstWSignIsOutRTypeAndIsComplete(workSignDto, lastPreviousDayWorkSign) || workSignOpSrv.isInRecordTypeWSign(workSignDto)
        )){
            return Optional.of(
                    new Alarm(
                            Stream.of(lastPreviousDayWorkSign,workSignDto).filter(Objects::nonNull)
                                    .collect(Collectors.toList()),
                            getKeyDescription(),
                            null
                    ));
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
        return workSignOpSrv.isInRecordTypeWSign(lastWorkSign) &&  workSignOpSrv.isOutRecordTypeWSign(firstLaterDayWorkSignsOfWork);
    }

    private boolean checkIfFirstWSignIsOutRTypeAndIsComplete(WorkSignDto firstWorkSignOfDay, WorkSignDto yesterdayLastWorkSign){
        return workSignOpSrv.isOutRecordTypeWSign(firstWorkSignOfDay) &&  workSignOpSrv.isInRecordTypeWSign(yesterdayLastWorkSign);
    }
    private boolean checkIfPreviousWSignIsOutNextWSignIsIn(WorkSignDto previousWorkSign, WorkSignDto nextWorkSign){
        return workSignOpSrv.isOutRecordTypeWSign(previousWorkSign) && workSignOpSrv.isInRecordTypeWSign(nextWorkSign);
    }

    private boolean checkIfPreviousWSignIsInNextWSignIsOut(WorkSignDto previousWorkSign, WorkSignDto nextWorkSign){
        return workSignOpSrv.isInRecordTypeWSign(previousWorkSign) && workSignOpSrv.isOutRecordTypeWSign(nextWorkSign);
    }
}
