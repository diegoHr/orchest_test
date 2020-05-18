package com.diego.hernando.orchestTest.business.weekReport.alarm.checker;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.helper.IncompleteWSignsOperationsService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BadRestWSignAlarmCheckerTest {

    private final WorkSignOperationsService workSignOpSrv = new WorkSignOperationsService();
    private final IncompleteWSignsOperationsService incompWSignsOpSrv = new IncompleteWSignsOperationsService(workSignOpSrv);
    private final BadRestWSignAlarmChecker alarmChecker = new BadRestWSignAlarmChecker(workSignOpSrv, incompWSignsOpSrv);

    private final WorkSignDto.WorkSignDtoBuilder workWSignBuilder = WorkSignDto.builder().type(WorkSignType.WORK);
    private final WorkSignDto.WorkSignDtoBuilder restWSignBuilder = WorkSignDto.builder().type(WorkSignType.REST);

    @Test
    public void test_getLevel () {
        assertThat(alarmChecker.getLevel(), is(AlarmLevel.ERROR));
    }

    @Test
    public void test_getKeyDescription () {
        assertThat(alarmChecker.getKeyDescription(), is("alarm.checker.bad.rest.worksign"));
    }

    @Test
    public void test_restWSigns_out_work_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 09:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(2)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(1), is(wSigns.get(3)));
    }

    @Test
    public void test_restWSign_IN_out_and_OUT_inside_work_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 09:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:15:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(2)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(1), is(wSigns.get(4)));
    }

    @Test
    public void test_restWSign_IN_inside_and_OUT_out_work_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:10:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:31:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(1)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(1), is(wSigns.get(3)));
    }

    @Test
    public void test_incompleteOutRestWSign_in_work_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:10:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:31:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 13:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(3)));
    }

    @Test
    public void test_incompleteInRestWSign_in_work_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:10:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:31:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 13:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(3)));
    }

    @Test
    public void test_incompleteOutRestWSign_out_work_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:10:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:31:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:30:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(4)));
    }

    @Test
    public void test_incompleteInRestWSign_out_work_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:10:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:31:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 15:30:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(4)));
    }

    @Test
    public void test_incompleteInRestWSign_out_work_and_2wSigns_out_work_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:10:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:30:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 11:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 12:31:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 15:30:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(2));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(6)));

        assertThat(alarms.get(1).getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(alarms.get(1).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(2)));
        assertThat(alarms.get(1).getWorkSignsTriggeredAlarm().get(1), is(wSigns.get(3)));
    }

    @Test
    public void test_wSigns_with_multiple_errors_check () {
        List<WorkSignDto> wSigns = Arrays.asList(
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:32:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:10:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:30:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 11:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 12:31:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 12:45:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 13:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 15:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 15:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 16:30:01")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 17:10:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 17:30:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 18:30:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 17:30:10")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 18:31:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 18:32:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 19:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 19:30:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);

        assertThat(alarms.size(), is(4));

        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(3));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(0)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(1), is(wSigns.get(10)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(2), is(wSigns.get(19)));

        assertThat(alarms.get(1).getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(alarms.get(1).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(4)));
        assertThat(alarms.get(1).getWorkSignsTriggeredAlarm().get(1), is(wSigns.get(5)));

        assertThat(alarms.get(2).getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(alarms.get(2).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(13)));
        assertThat(alarms.get(2).getWorkSignsTriggeredAlarm().get(1), is(wSigns.get(14)));

        assertThat(alarms.get(3).getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(alarms.get(3).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(15)));
        assertThat(alarms.get(3).getWorkSignsTriggeredAlarm().get(1), is(wSigns.get(17)));
    }

}
