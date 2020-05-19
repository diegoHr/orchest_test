package com.diego.hernando.orchestTest.testUtils;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Reader {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    public static List<WorkSignDto> readJsonDtos (String jsonDtos) throws JsonProcessingException {
        return objectMapper.readValue(jsonDtos,new TypeReference<List<WorkSignDto>>(){});
    }
}
