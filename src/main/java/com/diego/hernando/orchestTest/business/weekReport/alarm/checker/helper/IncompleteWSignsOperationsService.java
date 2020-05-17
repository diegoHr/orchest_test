package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.helper;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncompleteWSignsOperationsService {

    private final WorkSignOperationsService workSignOpSrv;

    @Autowired
    public IncompleteWSignsOperationsService(WorkSignOperationsService workSignOpSrv) {
        this.workSignOpSrv = workSignOpSrv;
    }

    public List<WorkSignDto> extractIncompleteWSigns(List<WorkSignDto> workSignDtos){
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
