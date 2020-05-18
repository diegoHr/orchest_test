package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.daily;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LimitMinimumInitWorkHourAlarmCheckerTest {

    private final WorkSignOperationsService wSignsOpSrv = new WorkSignOperationsService();
    private final DateOperationsService dateOpSrv = new DateOperationsService();

    private final LimitMinimumInitWorkHourAlarmChecker alarmChecker = new LimitMinimumInitWorkHourAlarmChecker(
            wSignsOpSrv, dateOpSrv);

    private final WorkSignDto.WorkSignDtoBuilder wSignBuilder = WorkSignDto.builder();

    private String getKeyDescription () {
        return "alarm.checker.limit.minimum_init_work_hour";
    }

    @Test
    public void test_getLevel () {
        assertThat(alarmChecker.getLevel(), is(AlarmLevel.WARNING));
    }

    @Test
    public void test_getKeyDescription () {
        assertThat(alarmChecker.getKeyDescription(), is(getKeyDescription()));
    }

    @Test
    public void test_wSigns_empty_check () {
        assertThat(alarmChecker.check(new ArrayList<>()).size(), is(0));
    }

    @Test
    public void test_first_workInWsign_Mon_7_59AM_alarm_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                wSignBuilder.recordType(WorkSignRecordType.IN).type(WorkSignType.WORK)
                        .date(parseDate("04/05/2020 07:59:00")).build() //08/05/2020 was monday
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getAlarmLevel(), is(AlarmLevel.WARNING));
        assertThat(alarms.get(0).getKeyDescription(), is(getKeyDescription()));
        assertThat(alarms.get(0).getDescriptionParams().length, is(2));
        assertThat(
                alarms.get(0).getDescriptionParams()[0],
                is(alarmChecker.getMinHourInit(
                        new DateTime(wSigns.get(0).getDate())).toDate()
                )
        );
        assertThat(
                alarms.get(0).getDescriptionParams()[1],
                is(alarmChecker.getMinHourInit(
                        new DateTime(wSigns.get(0).getDate())).toDate()
                )
        );
    }

    @Test
    public void test_first_workInWsign_Mon_8AM_not_alarm_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                wSignBuilder.recordType(WorkSignRecordType.IN).type(WorkSignType.WORK)
                        .date(parseDate("04/05/2020 08:00:00")).build() //08/05/2020 was monday
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(0));
    }

    @Test
    public void test_first_workInWsign_Fri_7AM_not_alarm_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                wSignBuilder.recordType(WorkSignRecordType.IN).type(WorkSignType.WORK)
                        .date(parseDate("08/05/2020 07:00:00")).build() //08/05/2020 was friday
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(0));
    }

    @Test
    public void test_first_workInWsign_Fri_6_59AM_alarm_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                wSignBuilder.recordType(WorkSignRecordType.IN).type(WorkSignType.WORK)
                        .date(parseDate("08/05/2020 06:59:00")).build() //08/05/2020 was friday
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getAlarmLevel(), is(AlarmLevel.WARNING));
        assertThat(alarms.get(0).getKeyDescription(), is(getKeyDescription()));
        assertThat(alarms.get(0).getDescriptionParams().length, is(2));
        assertThat(
                alarms.get(0).getDescriptionParams()[0],
                is(alarmChecker.getMinHourInit(
                        new DateTime(wSigns.get(0).getDate())).toDate()
                )
        );
        assertThat(
                alarms.get(0).getDescriptionParams()[1],
                is(alarmChecker.getMinHourInit(
                        new DateTime(wSigns.get(0).getDate())).toDate()
                )
        );
    }

    @Test
    public void test_first_workInWsign_Sat_7AM_not_alarm_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                wSignBuilder.recordType(WorkSignRecordType.IN).type(WorkSignType.WORK)
                        .date(parseDate("09/05/2020 07:00:00")).build() //08/05/2020 was saturday
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(0));
    }

    @Test
    public void test_first_workOutWsign_Sat_7AM_not_alarm_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                wSignBuilder.recordType(WorkSignRecordType.OUT).type(WorkSignType.WORK)
                        .date(parseDate("09/05/2020 07:00:00")).build() //09/05/2020 was saturday
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(0));
    }

    @Test
    public void test_first_workOutWsign_Mon_7AM_alarm_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                wSignBuilder.recordType(WorkSignRecordType.OUT).type(WorkSignType.WORK)
                        .date(parseDate("04/05/2020 07:00:00")).build() //04/05/2020 was monday
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(1));
    }

    @Test
    public void test_first_workOutWsign_Mon_7AM_second_workInWsign_Mon_8AM_not_alarm_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                wSignBuilder.recordType(WorkSignRecordType.OUT).type(WorkSignType.WORK)
                        .date(parseDate("04/05/2020 07:00:00")).build(), //04/05/2020 was monday
                wSignBuilder.recordType(WorkSignRecordType.IN).type(WorkSignType.WORK)
                        .date(parseDate("04/05/2020 08:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(0));
    }

    @Test
    public void test_first_workOutWsign_Mon_8AM_second_workInWsign_Mon_9AM_not_alarm_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                wSignBuilder.recordType(WorkSignRecordType.OUT).type(WorkSignType.WORK)
                        .date(parseDate("04/05/2020 08:00:00")).build(), //04/05/2020 was monday
                wSignBuilder.recordType(WorkSignRecordType.IN).type(WorkSignType.WORK)
                        .date(parseDate("04/05/2020 09:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(1));
    }


}
