package com.diego.hernando.orchestTest.business.weekReport.alarm;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlarmManagerServiceITest {

    @Autowired
    private AlarmManagerService alarmManager;

    private final WorkSignDto.WorkSignDtoBuilder workWSignBuilder = WorkSignDto.builder().type(WorkSignType.WORK);
    private final WorkSignDto.WorkSignDtoBuilder restWSignBuilder = WorkSignDto.builder().type(WorkSignType.REST);

    private final Locale esLocale = new Locale.Builder().setLanguage("es").build();

    @Test
    public void test_generateWeeklyAlarms(){
        List<WorkSignDto> weekWSigns = Arrays.asList(
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 05:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 05:30:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 05:45:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 06:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 06:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 07:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 07:30:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 07:45:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:45:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 09:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 09:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 22:45:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("05/05/2020 5:00:00")).build()
                );

        List<AlarmDto> weekAlarms = alarmManager.generateWeeklyAlarms(weekWSigns, esLocale);
        AlarmDto incompleteWorkWsingsAlarm = weekAlarms.stream().filter(alarmDto -> alarmDto.getDescription()
                .contentEquals("Existen 2 fichajes de trabajo erróneos, o pendientes de ser completados.")).findFirst().orElse(null);
        AlarmDto badRestWSignsAlarm1 = weekAlarms.stream().filter(alarmDto -> alarmDto.getDescription()
                .contentEquals("Existen 2 fichajes de descanso erróneos.")).findFirst().orElse(null);

        AlarmDto badRestWSignsAlarm2 = weekAlarms.stream().filter(alarmDto -> alarmDto.getDescription()
                .contentEquals("Existe 1 fichaje de descanso erróneo.")).findFirst().orElse(null);

        assertThat(weekAlarms.size(), is(3));
        assert incompleteWorkWsingsAlarm != null;
        assertThat(incompleteWorkWsingsAlarm.getLevel(), is(AlarmLevel.ERROR));
        assertThat(incompleteWorkWsingsAlarm.getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(incompleteWorkWsingsAlarm.getWorkSignsTriggeredAlarm().get(0), is(weekWSigns.get(0)));
        assertThat(incompleteWorkWsingsAlarm.getWorkSignsTriggeredAlarm().get(1), is(weekWSigns.get(1)));

        assert badRestWSignsAlarm1 != null;
        assertThat(badRestWSignsAlarm1.getLevel(), is(AlarmLevel.ERROR));
        assertThat(badRestWSignsAlarm1.getWorkSignsTriggeredAlarm().size(), is(2));
        assertThat(badRestWSignsAlarm1.getWorkSignsTriggeredAlarm().get(0), is(weekWSigns.get(2)));
        assertThat(badRestWSignsAlarm1.getWorkSignsTriggeredAlarm().get(1), is(weekWSigns.get(7)));

        assert badRestWSignsAlarm2 != null;
        assertThat(badRestWSignsAlarm2.getLevel(), is(AlarmLevel.ERROR));
        assertThat(badRestWSignsAlarm2.getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(badRestWSignsAlarm2.getWorkSignsTriggeredAlarm().get(0), is(weekWSigns.get(11)));
    }


    @Test
    public void test_monday_generateDailyAlarms(){
        List<WorkSignDto> mondayWSigns= Arrays.asList( //04/05/2020 was Monday
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 05:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:00:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 08:45:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 09:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 10:30:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 22:45:00")).build()
        );
        List<AlarmDto> dailyAlarms = alarmManager.generateDailyAlarms(mondayWSigns, esLocale);
        assertThat(dailyAlarms.size(), is(0));
    }

    @Test
    public void test_tuesday_generateDailyAlarms(){
        List<WorkSignDto> tuesdayWSigns= Arrays.asList( //05/05/2020 was Tuesday
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("05/05/2020 07:59:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("05/05/2020 08:45:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("05/05/2020 09:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("05/05/2020 20:30:00")).build()
        );

        List<AlarmDto> dailyAlarms = alarmManager.generateDailyAlarms(tuesdayWSigns, esLocale);
        assertThat(dailyAlarms.size(), is(2));

        AlarmDto limitMinimInitWorkAlarm = dailyAlarms.stream().filter(alarm-> alarm.getDescription()
                .contentEquals("Inició su jornada laboral antes de las 08:00, marcada como hora mínima para " +
                        "iniciar su jornada laboral los martes.")).findFirst().orElse(null);

        assertThat(limitMinimInitWorkAlarm, notNullValue());
        assertThat(limitMinimInitWorkAlarm.getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(limitMinimInitWorkAlarm.getWorkSignsTriggeredAlarm().get(0), is(tuesdayWSigns.get(0)));
        assertThat(limitMinimInitWorkAlarm.getLevel(), is(AlarmLevel.WARNING));

        AlarmDto limitHoursWorkedAlarm = dailyAlarms.stream().filter(alarm-> alarm.getDescription()
                .contentEquals("Superó el límite de 10H de trabajo el día mar, may 5, 2020."))
                .findFirst().orElse(null);
        assertThat(limitHoursWorkedAlarm, notNullValue());
        assertThat(limitHoursWorkedAlarm.getWorkSignsTriggeredAlarm().size(), is(4));
        assertThat(limitHoursWorkedAlarm.getWorkSignsTriggeredAlarm(), is(tuesdayWSigns));
        assertThat(limitHoursWorkedAlarm.getLevel(), is(AlarmLevel.WARNING));

    }

    @Test
    public void test_friday_generateDailyAlarms(){
        List<WorkSignDto> tuesdayWSigns= Arrays.asList( //08/05/2020 was Friday
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("08/05/2020 07:59:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("08/05/2020 08:45:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("08/05/2020 09:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("08/05/2020 20:30:00")).build()
        );

        List<AlarmDto> dailyAlarms = alarmManager.generateDailyAlarms(tuesdayWSigns, esLocale);
        assertThat(dailyAlarms.size(), is(1));

        AlarmDto limitHoursWorkedAlarm = dailyAlarms.get(0);

        assertThat(limitHoursWorkedAlarm.getDescription(), is("Superó el límite de 10H de trabajo" +
                " el día vie, may 8, 2020."));
        assertThat(limitHoursWorkedAlarm.getWorkSignsTriggeredAlarm().size(), is(4));
        assertThat(limitHoursWorkedAlarm.getWorkSignsTriggeredAlarm(), is(tuesdayWSigns));
        assertThat(limitHoursWorkedAlarm.getLevel(), is(AlarmLevel.WARNING));

    }

    @Test
    public void test_sunday_generateDailyAlarms(){
        List<WorkSignDto> tuesdayWSigns= Arrays.asList( //08/05/2020 was Friday
                workWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("10/05/2020 07:59:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.IN).date(parseDate("10/05/2020 08:45:00")).build(),
                restWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("10/05/2020 09:00:00")).build(),
                workWSignBuilder.recordType(WorkSignRecordType.OUT).date(parseDate("10/05/2020 20:30:00")).build()
        );

        List<AlarmDto> dailyAlarms = alarmManager.generateDailyAlarms(tuesdayWSigns, esLocale);
        assertThat(dailyAlarms.size(), is(1));
        assertThat(dailyAlarms.get(0).getDescription(), is("Superó el límite de 0H de trabajo el día " +
                "dom, may 10, 2020."));
        assertThat(dailyAlarms.get(0).getWorkSignsTriggeredAlarm(), is(tuesdayWSigns));
        assertThat(dailyAlarms.get(0).getLevel(), is(AlarmLevel.WARNING));
    }



}
