package com.diego.hernando.orchestTest.business.worksign.service;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.diego.hernando.orchestTest.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public class WorkSignReturnerByWeeksServiceTest {

    private final WorkSignReturnerByWeeksService workSignRetByWeekSrv = new WorkSignReturnerByWeeksService(
            null,
            null,
            new WorkSignOperationsService(), new DateOperationsService());

    private final WorkSignDto.WorkSignDtoBuilder builderBaseDto = WorkSignDto.builder().type(WorkSignType.WORK).recordType(WorkSignRecordType.IN)
            .employeeId("01").businessId("1").serviceId("service").date(parseDate("08/07/2020 00:00:00"));

    @Test
    public void get_last_IN_work_day_wSign_expected_from_prepared_day_list_of_wSigns_getLastWorkInDayWsign () {
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 10:00:00")).build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 11:00:00")).type(WorkSignType.REST).build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 15:00:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build());

        Optional<WorkSignDto> lastWorkInDayWsign = workSignRetByWeekSrv.getLastWorkInDayWsign(dtos);
        assertThat(lastWorkInDayWsign.isPresent(), is(true));
        assertThat(lastWorkInDayWsign.get().getDate(), is(parseDate("08/07/2020 10:00:00")));
        assertThat(lastWorkInDayWsign.get().getRecordType(), is(WorkSignRecordType.IN));
        assertThat(lastWorkInDayWsign.get().getType(), is(WorkSignType.WORK));
    }

    @Test
    public void test_get_list_empty_when_day_is_complete_getIncompleteWSignsOfDay () {
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 10:00:00")).build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 11:00:00")).type(WorkSignType.REST).build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 15:00:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build());

        assertThat(workSignRetByWeekSrv.getIncompleteWSignsOfDay(dtos).size(), is(0));
    }

    @Test
    public void test_get_list_empty_when_day_is_empty_getIncompleteWSignsOfDay () {
        List<WorkSignDto> dtos = new ArrayList<>();
        assertThat(workSignRetByWeekSrv.getIncompleteWSignsOfDay(dtos).size(), is(0));
    }

    @Test
    public void test_get_list_with_incomplete_wSigns_when_day_is_incomplete_getIncompleteWSignsOfDay () {
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 10:00:00")).build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 11:00:00")).type(WorkSignType.REST).build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 15:00:00")).type(WorkSignType.REST)
                .recordType(WorkSignRecordType.OUT).build());

        assertThat(workSignRetByWeekSrv.getIncompleteWSignsOfDay(dtos).size(), is(3));
    }

    @Test
    public void test_get_list_with_incomplete_wSigns_when_day_is_incomplete_getIncompleteWSignsOfDa () {
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 10:00:00")).build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 11:00:00")).type(WorkSignType.REST).build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 15:00:00")).type(WorkSignType.REST)
                .recordType(WorkSignRecordType.OUT).build());

        assertThat(workSignRetByWeekSrv.getIncompleteWSignsOfDay(dtos).size(), is(3));
    }

    @Test
    /**
     * It is a strange case, but if it were true, it would cause the loss of info if the data from the previous day
     * when they were incomplete.
     */
    public void check_week_has_not_wSigns_its_init_not_free_work_isFreeWorkWeekInit () {
        List<WorkSignDto> dtos = new ArrayList<>();
        assertThat(workSignRetByWeekSrv.isFreeWorkInitWeek(dtos), is(false));
    }

    @Test
    public void check_when_first_wSign_ofWeek_is_IN_WORK_its_init_is_free_work_isFreeWorkWeekInit () {
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.build());
        assertThat(workSignRetByWeekSrv.isFreeWorkInitWeek(dtos), is(true));
    }

    @Test
    public void check_when_first_wSign_ofWeek_is_OUT_WORK_its_init_is_not_free_work_isFreeWorkWeekInit () {
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.recordType(WorkSignRecordType.OUT).build());
        assertThat(workSignRetByWeekSrv.isFreeWorkInitWeek(dtos), is(false));
    }

    @Test
    public void check_when_first_wSign_ofWeek_is_IN_REST_its_init_is_not_free_work_isFreeWorkWeekInit () {
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.type(WorkSignType.REST).build());
        assertThat(workSignRetByWeekSrv.isFreeWorkInitWeek(dtos), is(false));
    }

    @Test
    public void check_when_first_wSign_ofWeek_is_OUT_REST_its_init_is_not_free_work_isFreeWorkWeekInit () {
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.recordType(WorkSignRecordType.OUT).type(WorkSignType.REST).build());
        assertThat(workSignRetByWeekSrv.isFreeWorkInitWeek(dtos), is(false));
    }

    @Test
    public void test_get_list_last_day_getLastDayWeekWsigns(){
        List<WorkSignDto> dtos = new ArrayList<>();
        dtos.add(builderBaseDto.build());
        dtos.add(builderBaseDto.date(parseDate("08/07/2020 10:00:00")).build());
        dtos.add(builderBaseDto.date(parseDate("08/08/2020 11:00:00")).type(WorkSignType.REST).build());
        dtos.add(builderBaseDto.date(parseDate("08/08/2020 15:00:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build());
        assertThat(workSignRetByWeekSrv.getLastDayWeekWsigns(dtos, parseDate("08/08/2020 11:00:00")).size(), is(2));
    }

    @Test
    public void test_list_empty_not_delete_any_wSign_deleteIncompleteEndWeekWSigns () {
        List<WorkSignDto> dtos = new ArrayList<>();
        assertThat(workSignRetByWeekSrv.deleteIncompleteEndWeekWSigns(dtos, new Date()).size(), is(0));
    }

    @Test
    public void test_day_complete_not_delete_any_wSign_deleteIncompleteEndWeekWSigns () {
        List<WorkSignDto> finalList = new ArrayList<>();
        finalList.add(builderBaseDto.build());
        finalList.add(builderBaseDto.date(parseDate("08/07/2020 10:00:00")).build());
        finalList.add(builderBaseDto.date(parseDate("08/07/2020 11:00:00")).type(WorkSignType.REST).build());
        finalList.add(builderBaseDto.date(parseDate("08/07/2020 15:00:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build());

        List<WorkSignDto> editableList = new ArrayList<>(finalList);

        assertThat(workSignRetByWeekSrv.deleteIncompleteEndWeekWSigns(editableList,parseDate("08/07/2020 15:00:00")), is(finalList));
        assertThat(editableList.size(), is(4));
    }

    @Test
    public void test_day_incomplete_delete_incomplete_wSigns_deleteIncompleteEndWeekWSigns () {
        List<WorkSignDto> finalList = new ArrayList<>();
        WorkSignDto dtoNotDeleted = builderBaseDto.build();
        finalList.add(dtoNotDeleted);
        finalList.add(builderBaseDto.date(parseDate("08/07/2020 10:00:00")).build());
        finalList.add(builderBaseDto.date(parseDate("08/07/2020 11:00:00")).type(WorkSignType.REST).build());
        finalList.add(builderBaseDto.date(parseDate("08/07/2020 15:00:00")).type(WorkSignType.REST)
                .recordType(WorkSignRecordType.IN).build());

        List<WorkSignDto> editableList = new ArrayList<>(finalList);

        assertThat(workSignRetByWeekSrv.deleteIncompleteEndWeekWSigns(editableList,parseDate("08/07/2020 15:00:00")),not(finalList));
        assertThat(editableList.get(0), is(dtoNotDeleted));
        assertThat(editableList.size(), is(1));
    }




}
