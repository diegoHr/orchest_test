package com.diego.hernando.orchestTest.business.worksign;

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

    public boolean isInRecordTypeWorkSign (WorkSignDto dto){
        return dto.getRecordType() == WorkSignRecordType.IN;
    }

    public boolean isOutRecordTypeWorkSign (WorkSignDto dto){
        return dto.getRecordType() == WorkSignRecordType.OUT;
    }

    public boolean isWorkTypeWorkSign (WorkSignDto dto){
        return dto.getType() == WorkSignType.WORK;
    }

    public boolean isRestTypeWorkSign (WorkSignDto dto){
        return dto.getType() == WorkSignType.REST;
    }

    public boolean isInRecordTypeAndWorkTypeWorkSign(WorkSignDto dto){
        return isInRecordTypeWorkSign(dto)
                && isWorkTypeWorkSign(dto);
    }

    public boolean isOutRecordTypeAndWorkTypeWorkSign(WorkSignDto dto){
        return isOutRecordTypeWorkSign(dto)
                && isWorkTypeWorkSign(dto);
    }

    public boolean isInRecordTypeAndRestTypeWorkSign(WorkSignDto dto){
        return isInRecordTypeWorkSign(dto)
                && isRestTypeWorkSign(dto);
    }

    public boolean isOutRecordTypeAndRestTypeWorkSign(WorkSignDto dto){
        return isOutRecordTypeWorkSign(dto)
                && isOutRecordTypeWorkSign(dto);
    }

}
