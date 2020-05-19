package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmDto;
import com.diego.hernando.orchestTest.configuration.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class WeekReportDto {
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = Constants.PATTERN_JSON_DATE)
    private Date initialDay;
    private Double hoursWorked;
    private List<DayReportDto> reportByDays;
    private List<AlarmDto> alarms;
}
