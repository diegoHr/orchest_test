package com.diego.hernando.orchestTest.business;

import com.diego.hernando.orchestTest.configuration.Constants;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkSignDto {
    private String businessId;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = Constants.PATTERN_JSON_DATE)
    private Date date;
    private String employeeId;
    private WorkSignRecordType recordType;
    private String serviceId;
    private WorkSignType type;
}
