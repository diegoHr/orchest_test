package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WeekReportDto {
    private Date initialDay;
    private Double workedHours;
    private List<DayReportDto> reportByDays;
    private List<AlarmDto> alarms;
}
