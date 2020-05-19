package com.diego.hernando.orchestTest.business.weekReport.alarm;

import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.daily.IDailyAlarmCheckerService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.weekly.IWeeklyAlarmCheckerService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class AlarmManagerServiceTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private List<IDailyAlarmCheckerService> dailyAlarmCheckers;

    @Mock
    private List<IWeeklyAlarmCheckerService> weeklyAlarmCheckers;

    @InjectMocks
    private AlarmManagerService alarmManager;

    @Test
    public void test_filterErrorAlarms() {
        List<AlarmDto> alarms = Arrays.asList(
                new AlarmDto(null,"alarm 1",AlarmLevel.ERROR),
                new AlarmDto(null,"alarm 2",AlarmLevel.ERROR),
                new AlarmDto(null,"alarm 1",AlarmLevel.WARNING),
                new AlarmDto(null,"alarm 1",AlarmLevel.ERROR),
                new AlarmDto(null,"alarm 1",AlarmLevel.WARNING),
                new AlarmDto(null,"alarm 1",AlarmLevel.ERROR)
        );

        List<AlarmDto> errorAlarms = alarmManager.filterErrorAlarms(alarms);
        assertThat(errorAlarms.size(), is(4));
        assertThat(errorAlarms.get(0), is(alarms.get(0)));
        assertThat(errorAlarms.get(1), is(alarms.get(1)));
        assertThat(errorAlarms.get(2), is(alarms.get(3)));
        assertThat(errorAlarms.get(3), is(alarms.get(5)));
    }

    @Test
    public void test_filterWarningAlarms() {
        List<AlarmDto> alarms = Arrays.asList(
                new AlarmDto(null,"alarm 1",AlarmLevel.ERROR),
                new AlarmDto(null,"alarm 2",AlarmLevel.ERROR),
                new AlarmDto(null,"alarm 3",AlarmLevel.WARNING),
                new AlarmDto(null,"alarm 4",AlarmLevel.ERROR),
                new AlarmDto(null,"alarm 5",AlarmLevel.WARNING),
                new AlarmDto(null,"alarm 6",AlarmLevel.ERROR)
        );

        List<AlarmDto> errorAlarms = alarmManager.filterWarningAlarms(alarms);
        assertThat(errorAlarms.size(), is(2));
        assertThat(errorAlarms.get(0), is(alarms.get(2)));
        assertThat(errorAlarms.get(1), is(alarms.get(4)));
    }

    @Test
    public void test_getWorkSignsThatTriggeredErrorAlarms() {
        WorkSignDto.WorkSignDtoBuilder wSignBuilder = WorkSignDto.builder().recordType(WorkSignRecordType.IN)
                .date(new Date()).type(WorkSignType.WORK).businessId("1").employeeId("01").serviceId("service");

        List<AlarmDto> alarms = Arrays.asList(
                new AlarmDto(Collections.singletonList(wSignBuilder.build()),"alarm 1",AlarmLevel.ERROR),
                new AlarmDto(Arrays.asList(
                        wSignBuilder.recordType(WorkSignRecordType.OUT).build(),
                        wSignBuilder.type(WorkSignType.REST).build()
                        ),"alarm 2",AlarmLevel.ERROR),
                new AlarmDto(new ArrayList<>(),"alarm 3",AlarmLevel.ERROR),
                new AlarmDto(Arrays.asList(
                        wSignBuilder.recordType(WorkSignRecordType.IN).businessId("2").build(),
                        wSignBuilder.type(WorkSignType.WORK).build()
                ),"alarm 4",AlarmLevel.WARNING)

        );

        List<WorkSignDto> wSignsTriggeredErrorAlarms = alarmManager.getWorkSignsThatTriggeredErrorAlarms(alarms);
        assertThat(wSignsTriggeredErrorAlarms.size(), is(3));
        assertThat(wSignsTriggeredErrorAlarms.get(0), is(alarms.get(0).getWorkSignsTriggeredAlarm().get(0)));
        assertThat(wSignsTriggeredErrorAlarms.get(1), is(alarms.get(1).getWorkSignsTriggeredAlarm().get(0)));
        assertThat(wSignsTriggeredErrorAlarms.get(2), is(alarms.get(1).getWorkSignsTriggeredAlarm().get(1)));

    }



}
