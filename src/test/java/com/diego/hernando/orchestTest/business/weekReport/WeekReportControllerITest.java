package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.restResponse.ErrorRestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WeekReportControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;


    @Test
    public void get_week_report ()  throws Exception{
        System.out.println(jsonWSigns);
        mockMvc.perform(MockMvcRequestBuilders.get("/weekreports/1/222222222?week=0"))
                .andExpect(status().isOk());
    }

    @Test
    public void get_week_reports () throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/worksigns/").contentType(MediaType.APPLICATION_JSON).content(jsonWSigns))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/weekreports/1/222222222/1/2018"))
                .andExpect(status().isOk()).andExpect(content().json(expectedResponse, true));
    }

    @Test
    public void when_month_is_value_not_allowed_return_internal_server_error() throws Exception{
        String errorMessage = "El parámetro de mes debe estar comprendido entre 1 y 12";
        ErrorRestResponse expectedResponse = ErrorRestResponse.builder().message(errorMessage).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/weekreports/1/222222222/13/2020"))
                .andExpect(status().isInternalServerError()).andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }



    private final String expectedResponse = "[\n" +
            "    {\n" +
            "        \"initialDay\": \"2017-12-31T23:00:00.000Z\",\n" +
            "        \"hoursWorked\": 30.5,\n" +
            "        \"reportByDays\": [\n" +
            "            {\n" +
            "                \"initDay\": \"2017-12-31T23:00:00.000+0000\",\n" +
            "                \"dayWorkSigns\": [\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-01T10:45:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"REST\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-01T15:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-01T18:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"initDay\": \"2018-01-01T23:00:00.000+0000\",\n" +
            "                \"dayWorkSigns\": [\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-02T08:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-02T10:30:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"REST\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-02T10:45:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"REST\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-02T13:30:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-02T15:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-02T18:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"initDay\": \"2018-01-02T23:00:00.000+0000\",\n" +
            "                \"dayWorkSigns\": [\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-03T08:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-03T10:30:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"REST\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-03T10:45:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"REST\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-03T13:30:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-03T15:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-03T18:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"initDay\": \"2018-01-03T23:00:00.000+0000\",\n" +
            "                \"dayWorkSigns\": [\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-04T08:00:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"IN\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-04T13:30:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"WORK\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        ],\n" +
            "        \"alarms\": [\n" +
            "            {\n" +
            "                \"workSignsTriggeredAlarm\": [\n" +
            "                    {\n" +
            "                        \"businessId\": \"1\",\n" +
            "                        \"date\": \"2018-01-01T10:45:00.000Z\",\n" +
            "                        \"employeeId\": \"222222222\",\n" +
            "                        \"recordType\": \"OUT\",\n" +
            "                        \"serviceId\": \"ALBASANZ\",\n" +
            "                        \"type\": \"REST\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"description\": \"Existe 1 fichaje de descanso erróneo.\",\n" +
            "                \"level\": \"ERROR\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"initialDay\": \"2018-01-07T23:00:00.000Z\",\n" +
            "        \"hoursWorked\": 0.0,\n" +
            "        \"reportByDays\": [],\n" +
            "        \"alarms\": []\n" +
            "    },\n" +
            "    {\n" +
            "        \"initialDay\": \"2018-01-14T23:00:00.000Z\",\n" +
            "        \"hoursWorked\": 0.0,\n" +
            "        \"reportByDays\": [],\n" +
            "        \"alarms\": []\n" +
            "    },\n" +
            "    {\n" +
            "        \"initialDay\": \"2018-01-21T23:00:00.000Z\",\n" +
            "        \"hoursWorked\": 0.0,\n" +
            "        \"reportByDays\": [],\n" +
            "        \"alarms\": []\n" +
            "    },\n" +
            "    {\n" +
            "        \"initialDay\": \"2018-01-28T23:00:00.000Z\",\n" +
            "        \"hoursWorked\": 0.0,\n" +
            "        \"reportByDays\": [],\n" +
            "        \"alarms\": []\n" +
            "    }\n" +
            "]";
    private final String jsonWSigns = "[{\"businessId\": \"1\",\n" +
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
            "  \"type\": \"WORK\"}]";




}
