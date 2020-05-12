package com.diego.hernando.orchestTest.business;

import com.diego.hernando.orchestTest.business.restResponse.IRestResponse;
import com.diego.hernando.orchestTest.business.restResponse.OkRestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkSignsControllerItTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void add_list_work_signs ()  throws Exception{
        String jsonWorkSigns = "[{\"businessId\": \"1\",\n" +
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
        IRestResponse responseExpected = OkRestResponse.builder().message("Se han almacenado 3 fichajes correctamente").build();
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/worksigns/").contentType(MediaType.APPLICATION_JSON).content(jsonWorkSigns))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(responseExpected), true));
    }


}


