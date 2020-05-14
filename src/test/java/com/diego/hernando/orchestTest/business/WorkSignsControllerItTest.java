package com.diego.hernando.orchestTest.business;

import com.diego.hernando.orchestTest.business.restResponse.IRestResponse;
import com.diego.hernando.orchestTest.business.restResponse.OkRestResponse;
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
public class WorkSignsControllerItTest {

    @Autowired
    private MockMvc mockMvc;

    private String jsonWorkSignsDisordered = "[{\"businessId\": \"1\",\n" +
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
            "  \"type\": \"REST\"}]";
    private String jsonWorkSignsOrdered = "[{\"businessId\": \"1\",\n" +
            "  \"date\": \"2018-01-01T08:00:00.000Z\",\n" +
            "  \"employeeId\": \"222222222\",\n" +
            "  \"recordType\": \"IN\",\n" +
            "  \"serviceId\": \"ALBASANZ\",\n" +
            "  \"type\": \"WORK\"},\n" +
            " {\"businessId\": \"1\",\n" +
            "  \"date\": \"2018-01-01T10:45:00.000Z\",\n" +
            "  \"employeeId\": \"222222222\",\n" +
            "  \"recordType\": \"OUT\",\n" +
            "  \"serviceId\": \"ALBASANZ\",\n" +
            "  \"type\": \"REST\"}," +
            " {\"businessId\": \"1\",\n" +
            "  \"date\": \"2018-01-01T13:30:00.000Z\",\n" +
            "  \"employeeId\": \"222222222\",\n" +
            "  \"recordType\": \"OUT\",\n" +
            "  \"serviceId\": \"ALBASANZ\",\n" +
            "  \"type\": \"WORK\"}]";

    @Test
    public void add_list_work_signs ()  throws Exception{
        IRestResponse responseExpected = OkRestResponse.builder().message("Se han almacenado 3 fichajes correctamente").build();
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/worksigns/").contentType(MediaType.APPLICATION_JSON).content(jsonWorkSignsDisordered))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(responseExpected), true));
    }

    @Test
    public void get_list_work_signs ()  throws Exception{
        add_list_work_signs();
        mockMvc.perform(MockMvcRequestBuilders.get("/worksigns/1/222222222"))
                .andExpect(status().isOk()).andExpect(content().json(jsonWorkSignsOrdered));
    }

}


