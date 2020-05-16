package com.diego.hernando.orchestTest.business.worksign.service;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class WorkSignOperationsService {

    private final Predicate<WorkSignDto> PREDICATE_IS_WORK_TYPE_WORKSIGN = new Predicate<WorkSignDto>(){

        @Override
        public boolean test(WorkSignDto dto) {
            return isWorkTypeWorkSign(dto);
        }
    };

    public Predicate<WorkSignDto> getPredicateIsWorkTypeWorkSign () {
        return PREDICATE_IS_WORK_TYPE_WORKSIGN;
    }

    public boolean isInRecordTypeWSign(WorkSignDto dto){
        return dto.getRecordType() == WorkSignRecordType.IN;
    }

    public boolean isOutRecordTypeWSign(WorkSignDto dto){
        return dto.getRecordType() == WorkSignRecordType.OUT;
    }

    public boolean isWorkTypeWorkSign (WorkSignDto dto){
        return dto.getType() == WorkSignType.WORK;
    }

    public boolean isRestTypeWorkSign (WorkSignDto dto){
        return dto.getType() == WorkSignType.REST;
    }

    public boolean isInRecordTypeAndWorkTypeWSign(WorkSignDto dto){
        return isInRecordTypeWSign(dto)
                && isWorkTypeWorkSign(dto);
    }

    public boolean isOutRecordTypeAndWorkTypeWSign(WorkSignDto dto){
        return isOutRecordTypeWSign(dto)
                && isWorkTypeWorkSign(dto);
    }

    public boolean isInRecordTypeAndRestTypeWSign(WorkSignDto dto){
        return isInRecordTypeWSign(dto)
                && isRestTypeWorkSign(dto);
    }

    public boolean isOutRecordTypeAndRestTypeWSign(WorkSignDto dto){
        return isOutRecordTypeWSign(dto)
                && isRestTypeWorkSign(dto);
    }

}
