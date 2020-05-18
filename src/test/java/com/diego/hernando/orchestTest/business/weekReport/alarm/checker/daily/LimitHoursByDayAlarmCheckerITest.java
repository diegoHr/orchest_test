package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.daily;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.weekReport.service.HoursWorkedCalculatorService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.hamcrest.number.IsCloseTo;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LimitHoursByDayAlarmCheckerITest {

    @Autowired
    private LimitHoursByDayAlarmChecker alarmChecker;
    @Autowired
    private HoursWorkedCalculatorService hoursWCalcSrv;

    private final WorkSignDto.WorkSignDtoBuilder workWSignBuilder = WorkSignDto.builder().type(WorkSignType.WORK);
    private final WorkSignDto.WorkSignDtoBuilder restWSignBuilder = WorkSignDto.builder().type(WorkSignType.REST);

    @Test
    public void test_getLevel () {
        assertThat(alarmChecker.getLevel(), is(AlarmLevel.WARNING));
    }

    @Test
    public void test_getKeyDescription () {
        assertThat(alarmChecker.getKeyDescription(), is("alarm.checker.limit.hours.day"));
    }

    @Test
    public void test_wSigns_empty_check () {
        assertThat(alarmChecker.check(new ArrayList<>()).size(), is(0));
    }

    @Test
    public void test_wSigns_only_with_one_inWSign_8AM_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(wSigns.get(0)));
        assertThat(alarms.get(0).getDescriptionParams()[0],
                is(alarmChecker.limitHoursOfWeek.get(
                        new DateTime(wSigns.get(0).getDate()).getDayOfWeek())
                )
        );

    }

    @Test
    public void test_wSigns_only_with_one_outWSign_8AM_check () {
        List<WorkSignDto> wSigns = Collections.singletonList(
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 08:00:00")).build()
        );

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(0));
    }

    @Test
    public void test_wSigns_that_add_9H_of_work_check () {
        List<WorkSignDto> wSigns = new ArrayList<>(Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 11:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 14:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 15:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 19:00:00")).build()
        ));

        List<Alarm> alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(0));
        assertThat(hoursWCalcSrv.calculateHoursWorked(wSigns), is(9D));

        wSigns.addAll(Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 22:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 23:00:00")).build()
        ));

        alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(0));
        assertThat(hoursWCalcSrv.calculateHoursWorked(wSigns), is(10D));

        wSigns.add(restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 23:30:00")).build());
        alarms = alarmChecker.check(wSigns);
        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm(), is(wSigns));
        assertThat(hoursWCalcSrv.calculateHoursWorked(wSigns), IsCloseTo.closeTo(10.499D, 0.01));
    }
}
