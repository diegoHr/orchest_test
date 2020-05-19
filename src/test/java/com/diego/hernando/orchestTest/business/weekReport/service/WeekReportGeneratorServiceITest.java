package com.diego.hernando.orchestTest.business.weekReport.service;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.WeekReportDto;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.transformWorkSignService.ITransformJsonCrudWorkSignService;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDateTime;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class WeekReportGeneratorServiceITest {

    @SpyBean
    private DateOperationsService dateOpSrv;

    @Autowired
    private WeekReportGeneratorService weekReportGenSrv;

    @Autowired
    @Qualifier("TransformJsonCrudWorkingSignService")
    private ITransformJsonCrudWorkSignService dtoToCrudWSignSrv;

    private final String defaultBusinessId = "01";
    private final String defaultEmployeeId = "01";

    private final Locale esLocale = new Locale.Builder().setLanguage("es").build();

    private final WorkSignDto.WorkSignDtoBuilder workWSignBuilder = WorkSignDto.builder()
            .businessId(defaultBusinessId).employeeId(defaultEmployeeId).serviceId("service").type(WorkSignType.WORK);
    private final WorkSignDto.WorkSignDtoBuilder restWSignBuilder = WorkSignDto.builder()
            .businessId(defaultBusinessId).employeeId(defaultEmployeeId).serviceId("service").type(WorkSignType.REST);

    @BeforeEach
    public void setUp(){
        when(dateOpSrv.getNow()).thenReturn(parseDateTime("06/05/2020 10:00:00"));
    }

    @Test
    public void test_empty_week_getWeekReport () {
        WeekReportDto weekReportDto = weekReportGenSrv.getWeekReport(0, defaultBusinessId, defaultEmployeeId,
                esLocale);
        assertThat(weekReportDto.getAlarms().size(), is(0));
        assertThat(weekReportDto.getHoursWorked(), is(0D));
        assertThat(weekReportDto.getInitialDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().size(), is(0));

    }

    @Test
    public void test_week_with_first_work_day_started_previous_week_getWeekReport(){
        List<WorkSignDto> weekWSigns = Arrays.asList(
                workWSignBuilder.date(parseDate("03/05/2020 10:00:00")).recordType(WorkSignRecordType.IN).build(),
                workWSignBuilder.date(parseDate("03/05/2020 15:00:00")).recordType(WorkSignRecordType.OUT).build(),
                workWSignBuilder.date(parseDate("03/05/2020 23:00:00")).recordType(WorkSignRecordType.IN).build(),

                restWSignBuilder.date(parseDate("04/05/2020 08:00:00")).recordType(WorkSignRecordType.IN).build(),
                restWSignBuilder.date(parseDate("04/05/2020 09:00:00")).recordType(WorkSignRecordType.OUT).build(),
                workWSignBuilder.date(parseDate("04/05/2020 10:00:00")).recordType(WorkSignRecordType.OUT).build(),

                workWSignBuilder.date(parseDate("07/05/2020 10:00:00")).recordType(WorkSignRecordType.IN).build(),
                workWSignBuilder.date(parseDate("07/05/2020 15:00:00")).recordType(WorkSignRecordType.OUT).build()
                );

        dtoToCrudWSignSrv.getListEntitiesSaved(weekWSigns);
        WeekReportDto weekReportDto = weekReportGenSrv.getWeekReport(0, defaultBusinessId, defaultEmployeeId,
                esLocale);

        assertThat(weekReportDto.getInitialDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getHoursWorked(), is(15D));
        assertThat(weekReportDto.getAlarms().size(), is(2));
        assertThat(weekReportDto.getAlarms().stream().anyMatch(alarm -> alarm.getLevel() == AlarmLevel.ERROR),
                is(false));

        assertThat(weekReportDto.getReportByDays().size(), is(3));
        assertThat(weekReportDto.getReportByDays().get(0).getInitDay(), is(parseDate("03/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().size(), is(1));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(0), is(weekWSigns.get(2)));

        assertThat(weekReportDto.getReportByDays().get(1).getInitDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().size(), is(3));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(0), is(weekWSigns.get(3)));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(1), is(weekWSigns.get(4)));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(2), is(weekWSigns.get(5)));

        assertThat(weekReportDto.getReportByDays().get(2).getInitDay(), is(parseDate("07/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().size(), is(2));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().get(0), is(weekWSigns.get(6)));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().get(1), is(weekWSigns.get(7)));
    }

    @Test
    public void test_week_with_first_work_incomplete_wSign_getWeekReport(){
        List<WorkSignDto> weekWSigns = Arrays.asList(
                restWSignBuilder.date(parseDate("04/05/2020 08:00:00")).recordType(WorkSignRecordType.IN).build(),
                restWSignBuilder.date(parseDate("04/05/2020 09:00:00")).recordType(WorkSignRecordType.OUT).build(),
                workWSignBuilder.date(parseDate("04/05/2020 10:00:00")).recordType(WorkSignRecordType.OUT).build(),

                workWSignBuilder.date(parseDate("07/05/2020 10:00:00")).recordType(WorkSignRecordType.IN).build(),
                workWSignBuilder.date(parseDate("07/05/2020 15:00:00")).recordType(WorkSignRecordType.OUT).build()
        );

        dtoToCrudWSignSrv.getListEntitiesSaved(weekWSigns);
        WeekReportDto weekReportDto = weekReportGenSrv.getWeekReport(0, defaultBusinessId, defaultEmployeeId,
                esLocale);

        assertThat(weekReportDto.getInitialDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getHoursWorked(), is(5D));
        assertThat(weekReportDto.getAlarms().size(), is(2));
        assertThat(weekReportDto.getAlarms().stream().anyMatch(alarm -> alarm.getLevel() == AlarmLevel.ERROR),
                is(true));

        assertThat(weekReportDto.getReportByDays().size(), is(2));

        assertThat(weekReportDto.getReportByDays().get(0).getInitDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().size(), is(3));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(0), is(weekWSigns.get(0)));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(1), is(weekWSigns.get(1)));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(2), is(weekWSigns.get(2)));

        assertThat(weekReportDto.getReportByDays().get(1).getInitDay(), is(parseDate("07/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().size(), is(2));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(0), is(weekWSigns.get(3)));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(1), is(weekWSigns.get(4)));
    }




    @Test
    public void test_week_with_last_work_day_with_incomplete_wSign_week_getWeekReport(){
        List<WorkSignDto> weekWSigns = Arrays.asList(
                workWSignBuilder.date(parseDate("04/05/2020 08:00:00")).recordType(WorkSignRecordType.IN).build(),
                restWSignBuilder.date(parseDate("04/05/2020 08:30:00")).recordType(WorkSignRecordType.IN).build(),
                restWSignBuilder.date(parseDate("04/05/2020 09:00:00")).recordType(WorkSignRecordType.OUT).build(),
                workWSignBuilder.date(parseDate("04/05/2020 10:00:00")).recordType(WorkSignRecordType.OUT).build(),

                workWSignBuilder.date(parseDate("07/05/2020 10:00:00")).recordType(WorkSignRecordType.IN).build(),
                workWSignBuilder.date(parseDate("07/05/2020 15:00:00")).recordType(WorkSignRecordType.OUT).build(),

                workWSignBuilder.date(parseDate("10/05/2020 10:00:00")).recordType(WorkSignRecordType.IN).build(),
                workWSignBuilder.date(parseDate("10/05/2020 15:00:00")).recordType(WorkSignRecordType.OUT).build(),
                workWSignBuilder.date(parseDate("10/05/2020 23:00:00")).recordType(WorkSignRecordType.IN).build()
        );

        dtoToCrudWSignSrv.getListEntitiesSaved(weekWSigns);
        WeekReportDto weekReportDto = weekReportGenSrv.getWeekReport(0, defaultBusinessId, defaultEmployeeId,
                esLocale);

        assertThat(weekReportDto.getInitialDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getHoursWorked(), is(11.5D));
        assertThat(weekReportDto.getAlarms().size(), is(2));
        assertThat(weekReportDto.getAlarms().stream().anyMatch(alarm -> alarm.getLevel() == AlarmLevel.ERROR),
                is(true));

        assertThat(weekReportDto.getReportByDays().size(), is(3));
        assertThat(weekReportDto.getReportByDays().get(0).getInitDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().size(), is(4));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(0), is(weekWSigns.get(0)));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(1), is(weekWSigns.get(1)));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(2), is(weekWSigns.get(2)));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(3), is(weekWSigns.get(3)));

        assertThat(weekReportDto.getReportByDays().get(1).getInitDay(), is(parseDate("07/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().size(), is(2));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(0), is(weekWSigns.get(4)));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(1), is(weekWSigns.get(5)));

        assertThat(weekReportDto.getReportByDays().get(2).getInitDay(), is(parseDate("10/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().size(), is(3));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().get(0), is(weekWSigns.get(6)));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().get(1), is(weekWSigns.get(7)));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().get(2), is(weekWSigns.get(8)));
    }

    @Test
    public void test_week_with_last_work_day_finished_next_week_getWeekReport(){
        List<WorkSignDto> weekWSigns = Arrays.asList(
                workWSignBuilder.date(parseDate("04/05/2020 08:00:00")).recordType(WorkSignRecordType.IN).build(),
                restWSignBuilder.date(parseDate("04/05/2020 08:30:00")).recordType(WorkSignRecordType.IN).build(),
                restWSignBuilder.date(parseDate("04/05/2020 09:00:00")).recordType(WorkSignRecordType.OUT).build(),
                workWSignBuilder.date(parseDate("04/05/2020 10:00:00")).recordType(WorkSignRecordType.OUT).build(),

                workWSignBuilder.date(parseDate("07/05/2020 10:00:00")).recordType(WorkSignRecordType.IN).build(),
                workWSignBuilder.date(parseDate("07/05/2020 15:00:00")).recordType(WorkSignRecordType.OUT).build(),

                workWSignBuilder.date(parseDate("10/05/2020 10:00:00")).recordType(WorkSignRecordType.IN).build(),
                workWSignBuilder.date(parseDate("10/05/2020 15:00:00")).recordType(WorkSignRecordType.OUT).build(),
                workWSignBuilder.date(parseDate("10/05/2020 23:00:00")).recordType(WorkSignRecordType.IN).build(),
                workWSignBuilder.date(parseDate("11/05/2020 08:00:00")).recordType(WorkSignRecordType.OUT).build()
        );

        dtoToCrudWSignSrv.getListEntitiesSaved(weekWSigns);
        WeekReportDto weekReportDto = weekReportGenSrv.getWeekReport(0, defaultBusinessId, defaultEmployeeId,
                esLocale);

        assertThat(weekReportDto.getInitialDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getHoursWorked(), is(11.5D));
        assertThat(weekReportDto.getAlarms().size(), is(1));
        assertThat(weekReportDto.getAlarms().stream().anyMatch(alarm -> alarm.getLevel() == AlarmLevel.ERROR),
                is(false));

        assertThat(weekReportDto.getReportByDays().size(), is(3));
        assertThat(weekReportDto.getReportByDays().get(0).getInitDay(), is(parseDate("04/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().size(), is(4));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(0), is(weekWSigns.get(0)));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(1), is(weekWSigns.get(1)));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(2), is(weekWSigns.get(2)));
        assertThat(weekReportDto.getReportByDays().get(0).getDayWorkSigns().get(3), is(weekWSigns.get(3)));

        assertThat(weekReportDto.getReportByDays().get(1).getInitDay(), is(parseDate("07/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().size(), is(2));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(0), is(weekWSigns.get(4)));
        assertThat(weekReportDto.getReportByDays().get(1).getDayWorkSigns().get(1), is(weekWSigns.get(5)));

        assertThat(weekReportDto.getReportByDays().get(2).getInitDay(), is(parseDate("10/05/2020 00:00:00")));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().size(), is(2));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().get(0), is(weekWSigns.get(6)));
        assertThat(weekReportDto.getReportByDays().get(2).getDayWorkSigns().get(1), is(weekWSigns.get(7)));
    }









}
