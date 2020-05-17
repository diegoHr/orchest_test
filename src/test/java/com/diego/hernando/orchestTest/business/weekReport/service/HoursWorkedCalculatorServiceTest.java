package com.diego.hernando.orchestTest.business.weekReport.service;

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

public class HoursWorkedCalculatorServiceTest {

    private WorkSignOperationsService wSignOpSrv = new WorkSignOperationsService();

    private HoursWorkedCalculatorService hoursWCalSrv = new HoursWorkedCalculatorService(wSignOpSrv);

    private WorkSignDto.WorkSignDtoBuilder builderDto = WorkSignDto.builder()
            .serviceId("service").businessId("01").employeeId("1").recordType(WorkSignRecordType.IN).type(WorkSignType.WORK)
            .date(parseDate("04/05/2020 10:00:00"));

    @Test
    public void test_calculate_worked_hours_of_1_days_complete_calculateHoursWorked () {
        List<WorkSignDto> wSigns = Arrays.asList(builderDto.build(),
                builderDto.type(WorkSignType.REST).date(parseDate("04/05/2020 12:00:00")).build(),
                builderDto.type(WorkSignType.REST).recordType(WorkSignRecordType.OUT)
                        .date(parseDate("04/05/2020 13:00:00")).build(),
                builderDto.type(WorkSignType.WORK).recordType(WorkSignRecordType.OUT)
                        .date(parseDate("04/05/2020 14:00:00")).build(),
                builderDto.type(WorkSignType.WORK).recordType(WorkSignRecordType.IN)
                        .date(parseDate("04/05/2020 15:00:00")).build(),
                builderDto.type(WorkSignType.WORK).recordType(WorkSignRecordType.OUT)
                        .date(parseDate("04/05/2020 20:00:00")).build()
        );

        assertThat(hoursWCalSrv.calculateHoursWorked(wSigns), is(8D));
    }
}
