package com.diego.hernando.orchestTest.business.restResponse;

import lombok.*;

@Data
@Builder
public class ErrorRestResponse implements IRestResponse {
    private final Status status = Status.ERROR;
    private String message;
}
