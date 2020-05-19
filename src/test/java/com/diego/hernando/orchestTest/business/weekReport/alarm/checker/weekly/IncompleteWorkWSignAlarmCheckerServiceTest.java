package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.weekly;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.helper.IncompleteWSignsOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.AlarmParameterFormattersFactoryService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static com.diego.hernando.orchestTest.testUtils.Reader.readJsonDtos;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IncompleteWorkWSignAlarmCheckerServiceTest {

    private final WorkSignOperationsService wSignOpSrv = new WorkSignOperationsService();
    private final IncompleteWSignsOperationsService incompWSignsOpSrv = new IncompleteWSignsOperationsService(wSignOpSrv);
    private final AlarmParameterFormattersFactoryService alarmParamFormattersFactory = Mockito.mock(AlarmParameterFormattersFactoryService.class);
    private final IncompleteWorkWSignAlarmCheckerService alarmChecker =
            new IncompleteWorkWSignAlarmCheckerService(wSignOpSrv, incompWSignsOpSrv, alarmParamFormattersFactory);

    @Test
    public void list_empty_not_trigger_alarm_check () {
        List<Alarm> alarms = alarmChecker.check(new ArrayList<>());
        assertThat(alarms.size(), is(0));
    }

    @Test
    public void list_with_one_IN_WSign_only_appear_1_time_in_alarm_check () throws Exception{
        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";

        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);
        List<Alarm> alarms = alarmChecker.check(dtos);
        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(dtos.get(0)));

    }

    @Test
    public void list_with_one_OUT_WSign_only_appear_1_time_in_alarm_check () throws Exception{
        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";

        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);
        List<Alarm> alarms = alarmChecker.check(dtos);
        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(dtos.get(0)));
    }

    @Test
    public void list_with_one_REST_WSign_not_appear_in_alarm_check () throws Exception{
        String jsonDtos = "[{\"businessId\": \"1\",\n" +
                "  \"date\": \"2017-12-31T18:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"OUT\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"REST\"}]";

        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);
        List<Alarm> alarms = alarmChecker.check(dtos);
        assertThat(alarms.size(), is(0));
    }


    @Test
    public void retrieve_1_alarm_with_8_check() throws Exception{

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
                "  \"type\": \"WORK\"}, \n"+
                " {\"businessId\": \"1\",\n" +
                "  \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
                "  \"employeeId\": \"222222222\",\n" +
                "  \"recordType\": \"IN\",\n" +
                "  \"serviceId\": \"ALBASANZ\",\n" +
                "  \"type\": \"WORK\"}]";

        List<WorkSignDto> dtos = readJsonDtos(jsonDtos);
        List<Alarm> alarms = alarmChecker.check(dtos);
        assertThat(alarms.size(), is(1));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().size(), is(8));
        assertThat(alarms.get(0).getKeyDescription(), is("alarm.checker.incomplete.work.worksign"));
        assertThat(alarms.get(0).getDescriptionParams()[0], is(8));
        assertThat(alarms.get(0).getAlarmLevel(), is(AlarmLevel.ERROR));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(0), is(dtos.get(0)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(1), is(dtos.get(1)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(2), is(dtos.get(2)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(3), is(dtos.get(5)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(4), is(dtos.get(6)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(5), is(dtos.get(7)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(6), is(dtos.get(8)));
        assertThat(alarms.get(0).getWorkSignsTriggeredAlarm().get(7), is(dtos.get(9)));
    }


}
