package com.diego.hernando.orchestTest.business.worksign.service;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.transformWorkSignService.ITransformJsonCrudWorkSignService;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import com.diego.hernando.orchestTest.model.service.ICrudWorkSignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class WorkSignReturnerByWeeksServiceITest {


    private WorkSignReturnerByWeeksService wSignRetByWeekSrv;

    @Autowired
    private WorkSignOperationsService wSignOpSrv;

    @Autowired
    private ICrudWorkSignService crudWorkSignService;

    @Autowired
    private ITransformJsonCrudWorkSignService transJsonCrudWSignSrv;

    @Spy
    private DateOperationsService dateOpSrv;


    private final WorkSignDto.WorkSignDtoBuilder builderBaseDto = WorkSignDto.builder().type(WorkSignType.WORK).recordType(WorkSignRecordType.IN)
            .employeeId("01").businessId("1").serviceId("service").date(parseDate("03/05/2020 00:00:00"));

    @BeforeEach
    public void setUp() {
        wSignRetByWeekSrv = new WorkSignReturnerByWeeksService(crudWorkSignService, transJsonCrudWSignSrv, wSignOpSrv, dateOpSrv);
        Mockito.when(dateOpSrv.getNow()).thenReturn(parseDateTime("05/05/2020 10:30:00"));
    }

    @Test
    public void test_get_incomplete_wSigns_of_prepared_set_getIncompleteWSignsOfPreviousDayOfWeek(){
        addTestWSignsOfIncompleteDay();
        List<WorkSignDto> incompWSignsPrevDayWeek = wSignRetByWeekSrv
                .getIncompleteWSignsOfPreviousDayOfWeek("1", "01",
                        dateOpSrv.transformWeekToInitWeekDate(0));

        assertThat(incompWSignsPrevDayWeek.size(), is(1));
        assertThat(incompWSignsPrevDayWeek.get(0), is(builderBaseDto.date(parseDate("03/05/2020 10:30:00"))
                .type(WorkSignType.WORK).recordType(WorkSignRecordType.IN).build()));
    }

    @Test
    public void test_get_empty_list_with_prepared_set_of_complete_day_getIncompleteWSignsOfPreviousDayOfWeek()  {
        addTestWsingsOfCompleteDay();
        List<WorkSignDto> compWSignsPrevDayWeek = wSignRetByWeekSrv
                .getIncompleteWSignsOfPreviousDayOfWeek("1", "01",
                        dateOpSrv.transformWeekToInitWeekDate(0));

        assertThat(compWSignsPrevDayWeek.size(), is(0));
    }

    @Test
    public void test_get_empty_list_with_worker_without_wSigns_in_system_getEmployeeWSignsOfWeek () {
        assertThat(wSignRetByWeekSrv.getEmployeeWSignsOfWeek("01", "1", 0).size(), is(0));
    }

    @Test
    public void test_get_list_with_previous_day_of_week_getEmployeeWSignsOfWeek () {
        addTestWSignsOfWeekWithPreviousDay();
        List<WorkSignDto> weekWSigns = wSignRetByWeekSrv.getEmployeeWSignsOfWeek("1", "01", 0);
        assertThat(weekWSigns.size(), is(12));
        assertThat(weekWSigns.get(0), is(builderBaseDto.date(parseDate("03/05/2020 10:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.IN).build()));
        assertThat(weekWSigns.get(weekWSigns.size()-1), is(builderBaseDto.date(parseDate("10/05/2020 19:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build()));
    }

    @Test
    public void test_get_list_only_with_data_of_week_getEmployeeWSignsOfWeek () {
        addTestWSignsOfWeekOnly();
        List<WorkSignDto> weekWSigns = wSignRetByWeekSrv.getEmployeeWSignsOfWeek("1", "01", 0);
        assertThat(weekWSigns.size(), is(10));
        assertThat(weekWSigns.get(0), is(builderBaseDto.date(parseDate("04/05/2020 10:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.IN).build()));
        assertThat(weekWSigns.get(weekWSigns.size()-1), is(builderBaseDto.date(parseDate("10/05/2020 19:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build()));
    }

    @Test
    public void test_get_list_only_with_data_of_week_without_incomplete_data_of_last_day_of_week_getEmployeeWSignsOfWeek () {
        addTestWSignsOfWeekWithoutIncompleteLastDayData();
        List<WorkSignDto> weekWSigns = wSignRetByWeekSrv.getEmployeeWSignsOfWeek("1", "01", 0);
        assertThat(weekWSigns.size(), is(6));
        assertThat(weekWSigns.get(0), is(builderBaseDto.date(parseDate("04/05/2020 10:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.IN).build()));
        assertThat(weekWSigns.get(weekWSigns.size()-1), is(builderBaseDto.date(parseDate("08/05/2020 12:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build()));
    }

    @Test
    public void test_week_data_without_incomplete_data_of_lastDay_with_data_of_previousDayWeek_getEmployeeWSignsOfWeek () {
        addTestWSignsOfWeekWithoutIncompLastDayDataWithPreviousDay();
        List<WorkSignDto> weekWSigns = wSignRetByWeekSrv.getEmployeeWSignsOfWeek("1", "01", 0);
        assertThat(weekWSigns.size(), is(8));
        assertThat(weekWSigns.get(0), is(builderBaseDto.date(parseDate("03/05/2020 10:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.IN).build()));
        assertThat(weekWSigns.get(weekWSigns.size()-1), is(builderBaseDto.date(parseDate("08/05/2020 12:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build()));
    }

    @Test
    public void test_with_only_incomplete_previous_day_info_getEmployeeWSignsOfWeek () {
        addTestWSignsOfIncompletePreviousDayOfWeek();
        List<WorkSignDto> weekWSigns = wSignRetByWeekSrv.getEmployeeWSignsOfWeek("1", "01", 0);
        assertThat(weekWSigns.size(), is(1));

        weekWSigns = wSignRetByWeekSrv.getEmployeeWSignsOfWeek("1", "01", 1);
        assertThat(weekWSigns.size(), is(5));
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

        assertThat(wSignRetByWeekSrv.deleteIncompleteEndWeekWSigns("1","01",editableList,parseDate("08/07/2020 15:00:00")), is(finalList));
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
        transJsonCrudWSignSrv.getListEntitiesSaved(finalList);
        transJsonCrudWSignSrv.getEntitySaved(builderBaseDto.date(parseDate("09/07/2020 15:00:00"))
                .type(WorkSignType.WORK).recordType(WorkSignRecordType.OUT).build());

        assertThat(wSignRetByWeekSrv.deleteIncompleteEndWeekWSigns("1", "01",
                editableList,parseDate("08/07/2020 15:00:00")), not(finalList));
        assertThat(editableList.get(0), is(dtoNotDeleted));
        assertThat(editableList.size(), is(1));
    }

    @Test
    public void test_list_empty_not_delete_any_wSign_deleteIncompleteEndWeekWSigns () {
        List<WorkSignDto> dtos = new ArrayList<>();
        assertThat(wSignRetByWeekSrv.deleteIncompleteEndWeekWSigns("1", "01", dtos, new Date()).size(), is(0));
    }


    private void addTestWSignsOfIncompletePreviousDayOfWeek (){
        transJsonCrudWSignSrv.getListEntitiesSaved(Arrays.asList(
                builderBaseDto.build(),
                builderBaseDto.date(parseDate("03/05/2020 05:00:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("03/05/2020 05:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 08:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build()
        ));
    }

    private void addTestWSignsOfWeekWithoutIncompLastDayDataWithPreviousDay(){
        transJsonCrudWSignSrv.getListEntitiesSaved(Arrays.asList(
                builderBaseDto.build(),
                builderBaseDto.date(parseDate("03/05/2020 05:00:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("03/05/2020 05:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 08:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDateTime("04/05/2020 00:00:00").withMillisOfSecond(0).toDate())
                        .type(WorkSignType.WORK).recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("02/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("05/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("06/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("07/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("08/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("10/05/2020 16:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("10/05/2020 17:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("10/05/2020 16:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("11/05/2020 08:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build()
        ));
    }

    private void addTestWSignsOfWeekWithoutIncompleteLastDayData () {
        transJsonCrudWSignSrv.getListEntitiesSaved(Arrays.asList(
                builderBaseDto.build(),
                builderBaseDto.date(parseDate("03/05/2020 05:00:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("03/05/2020 05:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 08:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("02/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("05/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("06/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("07/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("08/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("10/05/2020 16:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("10/05/2020 17:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("10/05/2020 16:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("11/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build()
        ));
    }

    private void addTestWSignsOfWeekOnly () {
        transJsonCrudWSignSrv.getListEntitiesSaved(Arrays.asList(
                builderBaseDto.build(),
                builderBaseDto.date(parseDate("03/05/2020 05:00:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("03/05/2020 05:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 08:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("02/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("05/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("06/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("07/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("08/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("10/05/2020 16:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("10/05/2020 17:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("10/05/2020 16:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("10/05/2020 19:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build()

        ));
    }


    private void addTestWSignsOfWeekWithPreviousDay () {
        transJsonCrudWSignSrv.getListEntitiesSaved(Arrays.asList(
                builderBaseDto.build(),
                builderBaseDto.date(parseDate("03/05/2020 05:00:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("03/05/2020 05:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 08:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDateTime("04/05/2020 00:00:00").withMillisOfSecond(0).toDate())
                        .type(WorkSignType.WORK).recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("02/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("05/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("06/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("07/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("08/05/2020 12:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("10/05/2020 16:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("10/05/2020 17:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("10/05/2020 16:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("10/05/2020 19:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build()

        ));
    }

    private void addTestWsingsOfCompleteDay () {
        addTestWSignsOfIncompleteDay();
        transJsonCrudWSignSrv.getEntitySaved(builderBaseDto.date(parseDate("03/05/2020 11:30:00")).type(WorkSignType.WORK)
                .recordType(WorkSignRecordType.OUT).build());
    }

    private void addTestWSignsOfIncompleteDay () {
        transJsonCrudWSignSrv.getListEntitiesSaved(Arrays.asList(
                builderBaseDto.build(),
                builderBaseDto.date(parseDate("03/05/2020 05:00:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("03/05/2020 05:30:00")).type(WorkSignType.REST)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 08:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.OUT).build(),
                builderBaseDto.date(parseDate("03/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),

                /*This wSign test the date selected to take previous day of week data from persistence,
                If this data is included in the data from the day before the previous week, then the wSigns would
                be complete and test would fail*/
                builderBaseDto.date(parseDateTime("04/05/2020 00:00:00").withMillisOfSecond(0).toDate())
                        .type(WorkSignType.WORK).recordType(WorkSignRecordType.OUT).build(),
                //Trash Data
                builderBaseDto.date(parseDate("02/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build(),
                builderBaseDto.date(parseDate("04/05/2020 10:30:00")).type(WorkSignType.WORK)
                        .recordType(WorkSignRecordType.IN).build()
        ));
    }


}
