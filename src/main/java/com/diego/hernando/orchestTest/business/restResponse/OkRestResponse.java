package com.diego.hernando.orchestTest.business.restResponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class OkRestResponse implements IRestResponse{
    @Getter
    private Status status = Status.OK;
    private String message;
    private Long optionalId;
}
