package com.diego.hernando.orchestTest.business.weekReport.alarm.checker;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IncompleteWorkDayAlarmCheckerServiceTest {

    private WorkSignOperationsService wSignOpSrv = new WorkSignOperationsService();

    private IncompleteWorkDayAlarmCheckerService alarmChecker = new IncompleteWorkDayAlarmCheckerService(wSignOpSrv);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void optionalAlarmIncompleteWSign_alarm_not_present_when_send_IN_and_OUT_WSigns () throws Exception {
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

        assertThat(alarmChecker.optionalAlarmIncompleteWSign(dtos.get(0),dtos.get(1)).isPresent(), is(false));
    }

    @Test
    public void optionalAlarmIncompleteWSign_alarm_not_present_when_send_OUT_and_IN_WSigns () throws Exception {
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

        assertThat(alarmChecker.optionalAlarmIncompleteWSign(dtos.get(0),dtos.get(1)).isPresent(), is(false));
    }

    @Test
    public void optionalAlarmIncompleteWSign_alarm_present_when_send_OUT_in_both_WSigns () throws Exception {
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
        optionalAlarmIncompletWSign_alarm_present_when_send_same_type_in_both_WSigns(jsonDtos);
    }

    @Test
    public void optionalAlarmIncompleteWSign_alarm_present_when_send_IN_in_both_WSigns () throws Exception {
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
        optionalAlarmIncompletWSign_alarm_present_when_send_same_type_in_both_WSigns(jsonDtos);
    }

    private void optionalAlarmIncompletWSign_alarm_present_when_send_same_type_in_both_WSigns (String jsonDtos) throws Exception{
        List<WorkSignDto> dtos =readJsonDtos(jsonDtos);

        Optional<Alarm> optionalAlarm =  alarmChecker.optionalAlarmIncompleteWSign(dtos.get(0),dtos.get(1));
        assertThat(optionalAlarm.isPresent(), is(true));
        assertThat(optionalAlarm.get().getKeyDescription(), is(alarmChecker.getKeyDescription()));
        assertThat(objectMapper.readTree(objectMapper.writeValueAsString(optionalAlarm.get().getWorkSignsTriggeredAlarm())),
                is(objectMapper.readTree(jsonDtos)));
    }

    @Test
    public void optionalAlarmIncompleteFirstWSign_alarm_not_present_when_first_wSign_is_IN_and_lastPreviousDayWSign_is_null () throws Exception{
        String jsonFirstDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto firstDto = objectMapper.readValue(jsonFirstDto, WorkSignDto.class);
        assertThat(alarmChecker.optionalAlarmIncompleteFirstWSign(firstDto,null).isPresent(), is(false));
    }

    @Test
    public void optionalAlarmIncompleteFirstWSign_alarm_is_present_when_first_wSign_is_OUT_and_lastPreviousDayWSign_is_null () throws Exception{
        String jsonFirstDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto firstDto = objectMapper.readValue(jsonFirstDto, WorkSignDto.class);
        Optional<Alarm> alarm = alarmChecker.optionalAlarmIncompleteFirstWSign(firstDto,null);
        assertThat(alarm.isPresent(), is(true));
        compareWSignWithJson(alarm.get().getWorkSignsTriggeredAlarm().get(0), jsonFirstDto);
        assertThat(alarm.get().getWorkSignsTriggeredAlarm().size(), is(1));
    }

    @Test
    public void optionalAlarmIncompleteFirstWSign_alarm_not_present_when_first_wSign_is_IN_and_lastPreviousDayWSign_is_IN () throws Exception{
        String jsonFirstDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonLastPreviousDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        WorkSignDto firstDto = objectMapper.readValue(jsonFirstDto, WorkSignDto.class);
        WorkSignDto lastPreviousDaytDto = objectMapper.readValue(jsonLastPreviousDayDto, WorkSignDto.class);

        assertThat(alarmChecker.optionalAlarmIncompleteFirstWSign(firstDto,lastPreviousDaytDto).isPresent(), is(false));
    }

    @Test
    public void optionalAlarmIncompleteFirstWSign_alarm_not_present_when_first_wSign_is_OUT_and_lastPreviousDayWSign_is_IN () throws Exception{
        String jsonFirstDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonLastPreviousDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        WorkSignDto firstDto = objectMapper.readValue(jsonFirstDto, WorkSignDto.class);
        WorkSignDto lastPreviousDaytDto = objectMapper.readValue(jsonLastPreviousDayDto, WorkSignDto.class);

        assertThat(alarmChecker.optionalAlarmIncompleteFirstWSign(firstDto,lastPreviousDaytDto).isPresent(), is(false));
    }

    @Test
    public void optionalAlarmIncompleteFirstWSign_alarm_present_when_first_wSign_is_OUT_and_lastPreviousDayWSign_is_OUT () throws Exception{
        String jsonFirstDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonLastPreviousDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        WorkSignDto firstDto = objectMapper.readValue(jsonFirstDto, WorkSignDto.class);
        WorkSignDto lastPreviousDaytDto = objectMapper.readValue(jsonLastPreviousDayDto, WorkSignDto.class);

        Optional<Alarm> alarm = alarmChecker.optionalAlarmIncompleteFirstWSign(firstDto,lastPreviousDaytDto);
        assertThat(alarm.isPresent(), is(true));
        compareWSignWithJson(alarm.get().getWorkSignsTriggeredAlarm().get(0), jsonLastPreviousDayDto);
        compareWSignWithJson(alarm.get().getWorkSignsTriggeredAlarm().get(1), jsonFirstDto);
        assertThat(alarm.get().getWorkSignsTriggeredAlarm().size(), is(2));
    }

    @Test
    public void optionalAlarmIncompleteLastWSign_alarm_not_present_when_last_wSign_is_OUT_and_firstNextDayWSign_is_null () throws Exception{
        String jsonLastDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto lastDto = objectMapper.readValue(jsonLastDto, WorkSignDto.class);
        assertThat(alarmChecker.optionalAlarmIncompleteLastWSign(lastDto,null).isPresent(), is(false));
    }

    @Test
    public void optionalAlarmIncompleteLastWSign_alarm_present_when_last_wSign_is_OUT_and_firstNextDayWSign_is_null () throws Exception{
        String jsonLastDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto lastDto = objectMapper.readValue(jsonLastDto, WorkSignDto.class);

        Optional<Alarm> alarm = alarmChecker.optionalAlarmIncompleteLastWSign(lastDto,null);
        assertThat(alarm.isPresent(), is(true));
        assertThat(objectMapper.readTree(
                objectMapper.writeValueAsString(alarm.get().getWorkSignsTriggeredAlarm().get(0))
        ),is(objectMapper.readTree(jsonLastDto)));
        assertThat(alarm.get().getWorkSignsTriggeredAlarm().size(), is(1));
    }

    @Test
    public void optionalAlarmIncompleteLastWSign_alarm_not_present_when_last_wSign_is_OUT_and_firstNextDayWSign_is_OUT () throws Exception{
        String jsonLastDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonFirstNextDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto lastDto = objectMapper.readValue(jsonLastDto, WorkSignDto.class);
        WorkSignDto firstNextDayDto = objectMapper.readValue(jsonFirstNextDayDto, WorkSignDto.class);
        assertThat(alarmChecker.optionalAlarmIncompleteLastWSign(lastDto,firstNextDayDto).isPresent(), is(false));
    }

    @Test
    public void optionalAlarmIncompleteLastWSign_alarm_not_present_when_last_wSign_is_IN_and_firstNextDayWSign_is_OUT () throws Exception{
        String jsonLastDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonFirstNextDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto lastDto = objectMapper.readValue(jsonLastDto, WorkSignDto.class);
        WorkSignDto firstNextDayDto = objectMapper.readValue(jsonFirstNextDayDto, WorkSignDto.class);
        assertThat(alarmChecker.optionalAlarmIncompleteLastWSign(lastDto,firstNextDayDto).isPresent(), is(false));
    }

    @Test
    public void optionalAlarmIncompleteLastWSign_alarm_not_present_when_last_wSign_is_IN_and_firstNextDayWSign_is_IN () throws Exception{
        String jsonLastDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonFirstNextDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto lastDto = objectMapper.readValue(jsonLastDto, WorkSignDto.class);
        WorkSignDto firstNextDayDto = objectMapper.readValue(jsonFirstNextDayDto, WorkSignDto.class);
        Optional<Alarm> optionalAlarm = alarmChecker.optionalAlarmIncompleteLastWSign(lastDto,firstNextDayDto);
        assertThat(optionalAlarm.isPresent(), is(true));
        assertThat(optionalAlarm.get().getWorkSignsTriggeredAlarm().size(), is(2));
        compareWSignWithJson(optionalAlarm.get().getWorkSignsTriggeredAlarm().get(0), jsonLastDto);
        compareWSignWithJson(optionalAlarm.get().getWorkSignsTriggeredAlarm().get(1), jsonFirstNextDayDto);
    }

    @Test
    public void optionalAlarmIncompleteWSign_throws_NullPointerException_when_nextWSign_is_null () throws Exception{
        String jsonPreviousDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto previousDto = objectMapper.readValue(jsonPreviousDto, WorkSignDto.class);
        assertThrows(NullPointerException.class,()->{
            alarmChecker.optionalAlarmIncompleteWSign(previousDto, null);
        });
    }

    @Test
    public void optionalAlarmIncompleteWSign_throws_NullPointerException_when_previousWSing_is_null () throws Exception{
        String jsonNextDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        WorkSignDto nextDto = objectMapper.readValue(jsonNextDto, WorkSignDto.class);
        assertThrows(NullPointerException.class,()->{
            alarmChecker.optionalAlarmIncompleteWSign(null, nextDto);
        });
    }

    @Test
    public void extractIncompleteWSignAndPackageInAlarms_workSignDtos_empty () throws Exception{
        String jsonLastPreviousDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonFirstNextDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        WorkSignDto lastPreviousDayDto = objectMapper.readValue(jsonLastPreviousDayDto, WorkSignDto.class);
        WorkSignDto firstNextDayDto = objectMapper.readValue(jsonFirstNextDayDto, WorkSignDto.class);

        assertThat(alarmChecker.extractIncompleteWSignAndPackageInAlarms(
                new ArrayList<>(),lastPreviousDayDto, firstNextDayDto).size(),
                is(0));

    }

    @Test
    public void extractIncompleteWSignAndPackageInAlarms_extract_2_alarms_from_two_wSigns_IN_and_firstNextDayWSign_IN () throws Exception{
        String jsonLastPreviousDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonFirstNextDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

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

        WorkSignDto lastPreviousDayDto = objectMapper.readValue(jsonLastPreviousDayDto, WorkSignDto.class);
        WorkSignDto firstNextDayDto = objectMapper.readValue(jsonFirstNextDayDto, WorkSignDto.class);
        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);

        assertThat(alarmChecker.extractIncompleteWSignAndPackageInAlarms(
                dtos,lastPreviousDayDto, firstNextDayDto).size(),
                is(2));

    }

    @Test
    public void extractIncompleteWSignAndPackageInAlarms_extract_2_alarms_from_two_wSigns_OUT_and_lastPreviousDayWSign_OUT () throws Exception{
        String jsonLastPreviousDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonFirstNextDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

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

        WorkSignDto lastPreviousDayDto = objectMapper.readValue(jsonLastPreviousDayDto, WorkSignDto.class);
        WorkSignDto firstNextDayDto = objectMapper.readValue(jsonFirstNextDayDto, WorkSignDto.class);
        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);

        assertThat(alarmChecker.extractIncompleteWSignAndPackageInAlarms(
                dtos,lastPreviousDayDto, firstNextDayDto).size(),
                is(2));
    }

    @Test
    public void extractIncompleteWSignAndPackageInAlarms_extract_3_alarms() throws Exception{
        String jsonLastPreviousDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonFirstNextDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        String jsonDtos = "[{\"businessId\": \"1\",\n" +
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
                "  \"type\": \"WORK\"}]";

        WorkSignDto lastPreviousDayDto = objectMapper.readValue(jsonLastPreviousDayDto, WorkSignDto.class);
        WorkSignDto firstNextDayDto = objectMapper.readValue(jsonFirstNextDayDto, WorkSignDto.class);
        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);

        assertThat(alarmChecker.extractIncompleteWSignAndPackageInAlarms(
                dtos,lastPreviousDayDto, firstNextDayDto).size(),
                is(3));
    }

    @Test
    public void check_retrieve_6_alarms () throws Exception{

        String jsonLastPreviousDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";
        String jsonFirstNextDayDto ="{\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}";

        String jsonDtos = "[{\"businessId\": \"1\",\n" +
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
                "  \"date\": \"2018-01-01T10:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n"+" {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T10:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n"+" {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T10:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n"+" {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-01T10:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"},\n"+" {\"businessId\": \"1\",\n" +
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
                "  \"type\": \"WORK\"}]";

        List<WorkSignDto> previousDtos = Arrays.asList(objectMapper.readValue(jsonLastPreviousDayDto, WorkSignDto.class));
        List<WorkSignDto> nextDtos = Arrays.asList(objectMapper.readValue(jsonFirstNextDayDto, WorkSignDto.class));
        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);

        assertThat(alarmChecker.check(dtos, previousDtos, nextDtos).size(), is(6));

    }

    private void compareWSignWithJson(WorkSignDto wSign, String json) throws JsonProcessingException {
        assertThat(
                objectMapper.readTree(
                        objectMapper.writeValueAsString(wSign)),
                is(objectMapper.readTree(json)));
    }


    private List<WorkSignDto> readJsonDtos (String jsonDtos) throws JsonProcessingException {
        return objectMapper.readValue(jsonDtos,new TypeReference<List<WorkSignDto>>(){});
    }

}
