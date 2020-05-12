package com.diego.hernando.orchestTest.business;

import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WorkSignDto {
    private String businessId;
    private Date date;
    private String employeeId;
    private WorkSignRecordType recordType;
    private String serviceId;
    private WorkSignType type;
}
