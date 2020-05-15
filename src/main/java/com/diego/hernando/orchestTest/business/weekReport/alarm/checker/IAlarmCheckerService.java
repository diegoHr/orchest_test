package com.diego.hernando.orchestTest.business.weekReport.alarm.checker;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;

import java.util.List;
import java.util.Optional;

public interface IAlarmCheckerService {
    String getKeyDescription();
    List<Alarm> check(List<WorkSignDto> workSignsToCheck,
                                List<WorkSignDto> previousWorkSigns,
                                List<WorkSignDto> laterWorkSigns);
}
