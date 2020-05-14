package com.diego.hernando.orchestTest.business.restResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OkRestResponse implements IRestResponse{
    private final Status status = Status.OK;
    private String message;
}
