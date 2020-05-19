package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.restResponse.ErrorRestResponse;
import com.diego.hernando.orchestTest.business.weekReport.service.WeekReportGeneratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@WebMvcTest(WeekReportController.class)
public class WeekReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @MockBean
    private WeekReportGeneratorService weekGenSrv;

    @MockBean
    private MessageSource messageSource;

    @Test
    public void when_week_not_is_integer_return_internal_server_error() throws Exception{
        String errorMessage = "Formato de semana no permitido";
        ErrorRestResponse expectedResponse = ErrorRestResponse.builder().message(errorMessage).build();

        Mockito.when(messageSource.getMessage(Mockito.anyString(),Mockito.any(), Mockito.any())).thenReturn(errorMessage);
        mockMvc.perform(MockMvcRequestBuilders.get("/weekreports/1/222222222?week=first"))
                .andExpect(status().isInternalServerError()).andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }

}
