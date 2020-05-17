package com.diego.hernando.orchestTest.business.weekReport.service;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.DayReportDto;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DayReportManagerServiceTest {

    private final DateOperationsService dateOpSrv = new DateOperationsService();
    private final DayReportManagerService dayReportManagerService = new DayReportManagerService(dateOpSrv);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void dayReports_of_week_without_work_getDayReportsFromWSignsOfWeek () {
        assertThat(dayReportManagerService.getDayReportsFromWSignsOfWeek(new ArrayList<WorkSignDto>()).size(), is(0));
    }

    @Test
    public void dayReports_of_week_with_work_between_2_days_getDayReportsFromWSignsOfWeek () {
        WorkSignDto.WorkSignDtoBuilder builder = WorkSignDto.builder().type(WorkSignType.WORK).recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 23:00:00"));
        List<WorkSignDto> wSigns = Arrays.asList(
                builder.build(),
                builder.recordType(WorkSignRecordType.OUT).date(parseDate("05/05/2020 8:00:00")).build()
        );
        List<DayReportDto> reports = dayReportManagerService.getDayReportsFromWSignsOfWeek(wSigns);
        assertThat(reports.size(), is(2));
        assertThat(reports.get(0).getDayWorkSigns().size(), is(1));
        assertThat(reports.get(0).getDayWorkSigns().get(0), is(wSigns.get(0)));
        assertThat(reports.get(0).getInitDay(), is(parseDate("04/05/2020 00:00:00")));

        assertThat(reports.get(1).getDayWorkSigns().size(), is(1));
        assertThat(reports.get(1).getDayWorkSigns().get(0), is(wSigns.get(1)));
        assertThat(reports.get(1).getInitDay(), is(parseDate("05/05/2020 00:00:00")));
    }

    @Test
    public void dayReports_of_week_with_2_work_days_not_consecutive_getDayReportsFromWSignsOfWeek () {
        WorkSignDto.WorkSignDtoBuilder builder = WorkSignDto.builder().type(WorkSignType.WORK).recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 10:00:00"));
        List<WorkSignDto> wSigns = Arrays.asList(
                builder.build(),
                builder.type(WorkSignType.REST).recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 11:00:00")).build(),
                builder.type(WorkSignType.REST).recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 11:30:00")).build(),
                builder.type(WorkSignType.WORK).recordType(WorkSignRecordType.OUT).date(parseDate("04/05/2020 17:30:00")).build(),

                builder.type(WorkSignType.WORK).recordType(WorkSignRecordType.IN).date(parseDate("07/05/2020 10:00:00")).build(),
                builder.type(WorkSignType.REST).recordType(WorkSignRecordType.IN).date(parseDate("07/05/2020 11:00:00")).build(),
                builder.type(WorkSignType.REST).recordType(WorkSignRecordType.OUT).date(parseDate("07/05/2020 11:30:00")).build(),
                builder.type(WorkSignType.WORK).recordType(WorkSignRecordType.OUT).date(parseDate("07/05/2020 17:30:00")).build()

        );
        List<DayReportDto> reports = dayReportManagerService.getDayReportsFromWSignsOfWeek(wSigns);
        assertThat(reports.size(), is(2));
        assertThat(reports.get(0).getDayWorkSigns().size(), is(4));
        assertThat(reports.get(0).getDayWorkSigns().get(0), is(wSigns.get(0)));
        assertThat(reports.get(0).getDayWorkSigns().get(3), is(wSigns.get(3)));
        assertThat(reports.get(0).getInitDay(), is(parseDate("04/05/2020 00:00:00")));

        assertThat(reports.get(1).getDayWorkSigns().size(), is(4));
        assertThat(reports.get(1).getDayWorkSigns().get(0), is(wSigns.get(4)));
        assertThat(reports.get(1).getDayWorkSigns().get(3), is(wSigns.get(7)));
        assertThat(reports.get(1).getInitDay(), is(parseDate("07/05/2020 00:00:00")));
    }

    @Test
    public void dayReports_with_wSigns_with_dates_equals_to_initialDayDate_getDayReportsFromWSignsOfWeek () {
        WorkSignDto.WorkSignDtoBuilder builder = WorkSignDto.builder().type(WorkSignType.WORK).recordType(WorkSignRecordType.IN).date(parseDate("04/05/2020 00:00:00"));
        List<WorkSignDto> wSigns = new ArrayList<>(Arrays.asList(
                builder.build(),
                builder.recordType(WorkSignRecordType.IN).date(parseDate("05/05/2020 00:00:00")).build(),
                builder.recordType(WorkSignRecordType.IN).date(parseDate("06/05/2020 00:00:00")).build()
        ));

        wSigns.add(2,builder.date(new Date(wSigns.get(wSigns.size()-1).getDate().getTime()-1)).build());
        List<DayReportDto> reports = dayReportManagerService.getDayReportsFromWSignsOfWeek(wSigns);
        assertThat(reports.size(), is(3));
        assertThat(reports.get(0).getDayWorkSigns().size(), is(1));
        assertThat(reports.get(0).getDayWorkSigns().get(0), is(wSigns.get(0)));
        assertThat(reports.get(0).getInitDay(), is(parseDate("04/05/2020 00:00:00")));

        assertThat(reports.get(1).getDayWorkSigns().size(), is(2));
        assertThat(reports.get(1).getDayWorkSigns().get(0), is(wSigns.get(1)));
        assertThat(reports.get(1).getDayWorkSigns().get(1), is(wSigns.get(2)));
        assertThat(reports.get(1).getInitDay(), is(parseDate("05/05/2020 00:00:00")));

        assertThat(reports.get(2).getDayWorkSigns().size(), is(1));
        assertThat(reports.get(2).getDayWorkSigns().get(0), is(wSigns.get(3)));
        assertThat(reports.get(2).getInitDay(), is(parseDate("06/05/2020 00:00:00")));
    }

    @Test
    public void dayReports_with_wSigns_with_dates_equals_to_finishDayDate_getDayReportsFromWSignsOfWeek () {
        WorkSignDto.WorkSignDtoBuilder builder = WorkSignDto.builder().type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.IN).date(parseDateTime("04/05/2020 23:59:59").withMillisOfSecond(999).toDate());
        List<WorkSignDto> wSigns = new ArrayList<>(Arrays.asList(
                builder.build(),
                builder.recordType(WorkSignRecordType.IN).date(parseDateTime("05/05/2020 23:59:59").withMillisOfSecond(999).toDate()).build(),
                builder.recordType(WorkSignRecordType.IN).date(parseDateTime("06/05/2020 23:59:59").withMillisOfSecond(999).toDate()).build()
        ));
        wSigns.add(builder.date(new Date(wSigns.get(wSigns.size()-1).getDate().getTime()+1)).build());

        List<DayReportDto> reports = dayReportManagerService.getDayReportsFromWSignsOfWeek(wSigns);
        assertThat(reports.size(), is(4));
        assertThat(reports.get(0).getDayWorkSigns().size(), is(1));
        assertThat(reports.get(0).getDayWorkSigns().get(0), is(wSigns.get(0)));
        assertThat(reports.get(0).getInitDay(), is(parseDate("04/05/2020 00:00:00")));

        assertThat(reports.get(1).getDayWorkSigns().size(), is(1));
        assertThat(reports.get(1).getDayWorkSigns().get(0), is(wSigns.get(1)));
        assertThat(reports.get(1).getInitDay(), is(parseDate("05/05/2020 00:00:00")));

        assertThat(reports.get(2).getDayWorkSigns().size(), is(1));
        assertThat(reports.get(2).getDayWorkSigns().get(0), is(wSigns.get(2)));
        assertThat(reports.get(2).getInitDay(), is(parseDate("06/05/2020 00:00:00")));

        assertThat(reports.get(3).getDayWorkSigns().size(), is(1));
        assertThat(reports.get(3).getDayWorkSigns().get(0), is(wSigns.get(3)));
        assertThat(reports.get(3).getInitDay(), is(parseDate("07/05/2020 00:00:00")));
    }

    @Test
    public void dayReports_of_test_set_getDayReportsFromWSignsOfWeek () throws Exception{
        String json = getJsonTestSet();
        List<WorkSignDto> wSigns = objectMapper.readValue(json,new TypeReference<List<WorkSignDto>>(){});

        List<DayReportDto> reports = dayReportManagerService.getDayReportsFromWSignsOfWeek(wSigns);
        assertThat(reports.size(), is(10));
        assertThat(reports.get(0).getDayWorkSigns().get(0), is(wSigns.get(0)));
        assertThat(reports.get(0).getInitDay(), is(parseDate("01/01/2018 00:00:00")));
        assertThat(reports.get(1).getInitDay(), is(parseDate("02/01/2018 00:00:00")));
        assertThat(reports.get(2).getInitDay(), is(parseDate("03/01/2018 00:00:00")));
        assertThat(reports.get(3).getInitDay(), is(parseDate("04/01/2018 00:00:00")));
        assertThat(reports.get(4).getInitDay(), is(parseDate("05/01/2018 00:00:00")));
        assertThat(reports.get(5).getInitDay(), is(parseDate("08/01/2018 00:00:00")));
        assertThat(reports.get(6).getInitDay(), is(parseDate("09/01/2018 00:00:00")));
        assertThat(reports.get(7).getInitDay(), is(parseDate("10/01/2018 00:00:00")));
        assertThat(reports.get(8).getInitDay(), is(parseDate("11/01/2018 00:00:00")));
        assertThat(reports.get(9).getInitDay(), is(parseDate("12/01/2018 00:00:00")));
    }

    public String getJsonTestSet () {
        return "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T10:45:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T10:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T10:45:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-03T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-03T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-03T10:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-03T10:45:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-03T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-03T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-04T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-04T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-04T10:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-04T10:45:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-04T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-04T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-05T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-05T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-05T10:45:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-05T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-05T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-08T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-08T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-08T10:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-08T10:45:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-08T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-08T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-09T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-09T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-09T10:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-09T10:45:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-09T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-09T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-10T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-10T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-10T10:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-10T10:45:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-10T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-10T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-11T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-11T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-11T10:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-11T15:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-11T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-12T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-12T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";
    }

}
