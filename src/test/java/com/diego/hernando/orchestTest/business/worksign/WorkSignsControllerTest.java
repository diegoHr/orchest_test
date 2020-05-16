package com.diego.hernando.orchestTest.business.worksign;

import com.diego.hernando.orchestTest.business.restResponse.ErrorRestResponse;
import com.diego.hernando.orchestTest.business.worksign.service.transformWorkSignService.ImplTransformJsonCrudWorkSignService;
import com.diego.hernando.orchestTest.model.service.ImplCrudJpaWorkSignService;
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
@WebMvcTest(WorkSignsController.class)
public class WorkSignsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @MockBean
    private ImplTransformJsonCrudWorkSignService transformJsonCrudService;

    @MockBean
    private ImplCrudJpaWorkSignService crudService;

    @MockBean
    private MessageSource messageSource;


    @Test
    public void when_get_worksings_and_crud_service_return_no_controled_exception_return_internal_server_error() throws Exception{
        String errorMessage = "Not controlled exception";
        ErrorRestResponse expectedResponse = ErrorRestResponse.builder().message(errorMessage).build();
        Mockito.when(crudService.findWorkSignsOfWorker(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new RuntimeException(errorMessage));
        Mockito.when(messageSource.getMessage(Mockito.anyString(),Mockito.any(), Mockito.any())).thenReturn(errorMessage);
        mockMvc.perform(MockMvcRequestBuilders.get("/worksigns/1/222222222"))
                .andExpect(status().isInternalServerError()).andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));
    }
}
