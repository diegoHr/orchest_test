package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class WeekReportDto {
    private Date initialDay;
    private Double hoursWorked;
    private List<DayReportDto> reportByDays;
    private List<AlarmDto> alarms;
}
