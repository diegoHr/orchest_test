package com.diego.hernando.orchestTest.business.weekReport.alarm;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AlarmDto {
    private List<WorkSignDto> workSignsTriggeredAlarm;
    private String description;

}
