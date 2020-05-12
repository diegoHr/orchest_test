package com.diego.hernando.orchestTest.business.restResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class ErrorRestResponse implements IRestResponse {
    @Getter
    private Status status = Status.ERROR;
    private String message;
    private Long optionalId;
}
