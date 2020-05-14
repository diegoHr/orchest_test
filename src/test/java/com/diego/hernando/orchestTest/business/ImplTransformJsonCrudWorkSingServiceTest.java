package com.diego.hernando.orchestTest.business;


import com.diego.hernando.orchestTest.business.service.ImplTransformJsonCrudWorkSignService;
import com.diego.hernando.orchestTest.configuration.Constants;
import com.diego.hernando.orchestTest.model.WorkSignEntity;
import com.diego.hernando.orchestTest.model.WorkSignRecordType;
import com.diego.hernando.orchestTest.model.WorkSignType;
import com.diego.hernando.orchestTest.model.service.ICrudWorkSignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;


@ExtendWith(MockitoExtension.class)
public class ImplTransformJsonCrudWorkSingServiceTest {

    @Mock
    private ICrudWorkSignService crudService;

    @InjectMocks
    private ImplTransformJsonCrudWorkSignService service;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private long epochDate = 1577872800000L;
    private String stringDate ="2020-01-1T10:00:00.000Z";
    private String jsonDto = "{\"businessId\": \"1\",\n" +
            "  \"date\": \""+stringDate+"\",\n" +
            "  \"employeeId\": \"01\",\n" +
            "  \"recordType\": \"IN\",\n" +
            "  \"serviceId\": \"service\",\n" +
            "  \"type\": \"WORK\"}";;


    @Test
    public void conversion_from_json_to_dto () throws Exception{

        WorkSignDto dto = jsonMapper.readValue(jsonDto, WorkSignDto.class);
        assertThat(dto.getDate().getTime(), is(epochDate));
        assertThat(dto.getBusinessId(), is("1"));
        assertThat(dto.getEmployeeId(), is("01"));
        assertThat(dto.getRecordType(), is(WorkSignRecordType.IN));
        assertThat(dto.getServiceId(), is("service"));
        assertThat(dto.getType(), is (WorkSignType.WORK));
    }

    @Test
    public void transform_crud_entity_to_json_dto () throws Exception{
        SimpleDateFormat formater = new SimpleDateFormat(Constants.PATTERN_JSON_DATE);
        Date dateTime = formater.parse(stringDate);//formatter.parseDateTime(stringDate);

        WorkSignEntity entity = WorkSignEntity.builder().employeeId("01").
                recordType(WorkSignRecordType.IN).
                date(dateTime).
                type(WorkSignType.WORK).
                serviceId("service").
                businessId("1").
                id(0L).build();

        WorkSignDto dto = service.getDto(entity);
        assertThat(jsonMapper.readTree(jsonMapper.writeValueAsString(dto)), is(jsonMapper.readTree(jsonDto)) );
    }


    @Test
    public void transform_json_dto_to_crud_entity () throws Exception{
        WorkSignDto dto = jsonMapper.readValue(jsonDto, WorkSignDto.class);
        WorkSignEntity entity = service.getEntity(dto);

        assertThat(entity.getId(), nullValue());
        assertThat(entity.getDate().getTime(), is(epochDate));
        assertThat(entity.getBusinessId(), is("1"));
        assertThat(entity.getEmployeeId(), is("01"));
        assertThat(entity.getRecordType(), is(WorkSignRecordType.IN));
        assertThat(entity.getType(), is(WorkSignType.WORK));
        assertThat(entity.getServiceId(), is("service"));
    }



}
