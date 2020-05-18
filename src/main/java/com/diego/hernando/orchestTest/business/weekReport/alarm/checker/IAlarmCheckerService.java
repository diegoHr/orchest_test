package com.diego.hernando.orchestTest.business.weekReport.alarm.checker;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.IAlarmParameterFormatter;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;

import java.util.List;

public interface IAlarmCheckerService {
    String getKeyDescription();
    List<Alarm> check(List<WorkSignDto> workSignsToCheck);
    AlarmLevel getLevel ();
    List<IAlarmParameterFormatter<Object>> getParameterFormatters();
}
