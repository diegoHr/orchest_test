package com.diego.hernando.orchestTest.business.weekReport.alarm;

import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.IAlarmCheckerService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.daily.IDailyAlarmCheckerService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.weekly.IWeeklyAlarmCheckerService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AlarmManagerService {

    @Autowired
    private List<IDailyAlarmCheckerService> dailyAlarmCheckers;

    @Autowired
    private List<IWeeklyAlarmCheckerService> weeklyAlarmCheckers;

    @Autowired
    private MessageSource messageSource;

    public List<AlarmDto> generateWeeklyAlarms (List<WorkSignDto> weeklyWSigns, Locale locale){
        return generateAlarms(weeklyWSigns, weeklyAlarmCheckers, locale);
    }

    public List<AlarmDto> generateDailyAlarms (List<WorkSignDto> dailyWSigns, Locale locale){
        return generateAlarms(dailyWSigns, dailyAlarmCheckers, locale);
    }

    public List<AlarmDto> filterErrorAlarms (List<AlarmDto> alarms) {
        return filterAlarmsByLevel(alarms, AlarmLevel.ERROR);
    }

    public List<AlarmDto> filterWarningAlarms (List<AlarmDto> alarms){
        return filterAlarmsByLevel(alarms, AlarmLevel.WARNING);
    }

    public List<WorkSignDto> getWorkSignsThatTriggeredErrorAlarms (List<AlarmDto> alarmDtos){
        return filterErrorAlarms(alarmDtos).stream().map(AlarmDto::getWorkSignsTriggeredAlarm).flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private <C extends IAlarmCheckerService> List<AlarmDto>  generateAlarms (List<WorkSignDto> wSigns, List<C> alarmCheckers, Locale locale){
        return alarmCheckers.stream().map(weeklyChecker -> weeklyChecker.check(wSigns)).flatMap(List::stream)
                .map(alarm -> alarm.getDto(messageSource, locale)).collect(Collectors.toList());
    }

    private List<AlarmDto> filterAlarmsByLevel (List<AlarmDto> alarms, AlarmLevel level){
        return alarms.stream().filter(alarm -> alarm.getLevel() == level).collect(Collectors.toList());
    }
}
