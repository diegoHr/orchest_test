package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.helper;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.diego.hernando.orchestTest.testUtils.Reader.readJsonDtos;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IncompleteWSignsOperationsServiceTest {

    private final WorkSignOperationsService wSignOpSrv = new WorkSignOperationsService();
    private final IncompleteWSignsOperationsService incompWSignsOpSrv = new IncompleteWSignsOperationsService(wSignOpSrv);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void incompleteWSign_not_present_when_send_IN_and_OUT_WSigns_getIncompleteWSign () throws Exception {
        String jsonDtos = "[{\"businessId\": \"1\",\n" +
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
                "  \"type\": \"WORK\"}]";
        List<WorkSignDto> dtos =readJsonDtos(jsonDtos);

        assertThat(incompWSignsOpSrv.getIncompleteWSign(dtos.get(0),dtos.get(1)).isPresent(), is(false));
    }

    @Test
    public void incompleteWSign_not_present_when_send_OUT_and_IN_WSigns_getIncompleteWSign () throws Exception {
        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";
        List<WorkSignDto> dtos =readJsonDtos(jsonDtos);

        assertThat(incompWSignsOpSrv.getIncompleteWSign(dtos.get(0),dtos.get(1)).isPresent(), is(false));
    }

    @Test
    public void incompleteWSign_present_when_send_OUT_in_both_WSigns_getIncompleteWSign () throws Exception {
        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";
        List<WorkSignDto> dtos =readJsonDtos(jsonDtos);

        Optional<WorkSignDto> optionalIncompleteWSign=  incompWSignsOpSrv.getIncompleteWSign(dtos.get(0),dtos.get(1));
        assertThat(optionalIncompleteWSign.isPresent(), is(true));
        assertThat(optionalIncompleteWSign.get(), is(dtos.get(1)));
    }

    @Test
    public void incompleteWSign_present_when_send_IN_in_both_WSigns_getIncompleteWSign () throws Exception {
        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";
        List<WorkSignDto> dtos =readJsonDtos(jsonDtos);

        Optional<WorkSignDto> optionalIncompleteWSign=  incompWSignsOpSrv.getIncompleteWSign(dtos.get(0),dtos.get(1));
        assertThat(optionalIncompleteWSign.isPresent(), is(true));
        assertThat(optionalIncompleteWSign.get(), is(dtos.get(0)));
    }

    @Test
    public void throwsNullPointerException_when_first_wSign_is_IN_and_lastPreviousDayWSign_is_null_getIncompleteWSign () throws Exception{
        String jsonFirstDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto firstDto = objectMapper.readValue(jsonFirstDto, WorkSignDto.class);
        assertThrows(NullPointerException.class, ()->{
            incompWSignsOpSrv.getIncompleteWSign(firstDto,null);
        });
    }

    @Test
    public void throws_NullPointerException_when_nextWSign_is_null_getIncompleteWSign () throws Exception{
        String jsonPreviousDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto previousDto = objectMapper.readValue(jsonPreviousDto, WorkSignDto.class);
        assertThrows(NullPointerException.class,()->{
            incompWSignsOpSrv.getIncompleteWSign(previousDto, null);
        });
    }

    @Test
    public void throws_NullPointerException_when_previousWSing_is_null_getIncompleteWSign () throws Exception{
        String jsonNextDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        WorkSignDto nextDto = objectMapper.readValue(jsonNextDto, WorkSignDto.class);
        assertThrows(NullPointerException.class,()->{
            incompWSignsOpSrv.getIncompleteWSign(null, nextDto);
        });
    }

    @Test
    public void workSignDtos_empty_extractIncompleteWSigns () throws Exception{
        assertThat(incompWSignsOpSrv.extractIncompleteWSigns(
                new ArrayList<>()).size(),
                is(0));
    }

    @Test
    public void extract_4_icompleteWSigns_from_three_wSigns_IN_extractIncompleteWSigns () throws Exception{

        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}, \n"+
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}," +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";

        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);

        assertThat(incompWSignsOpSrv.extractIncompleteWSigns(dtos).size(), is(4));
    }

    @Test
    public void extract_4_incompleteIWSigns_from_two_wSigns_OUT_and_lastPreviousDayWSign_OUT_extractIncompleteWSigns () throws Exception{

        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}, \n"+
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}, \n"+
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";

        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);

        assertThat(incompWSignsOpSrv.extractIncompleteWSigns(dtos).size(), is(4));
    }

    @Test
    public void extract_5_incompleteWSigns_extractIncompleteWSigns() throws Exception{

        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n"+
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n" +
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T10:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n"+
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n"+
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";

        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);

        assertThat(incompWSignsOpSrv.extractIncompleteWSigns(dtos).size(), is(5));
    }
}
