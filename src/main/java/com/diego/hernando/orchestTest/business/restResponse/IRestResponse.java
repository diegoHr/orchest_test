package com.diego.hernando.orchestTest.business.restResponse;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface IRestResponse {

    Status getStatus ();
    String getMessage ();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    enum Status {
        OK,ERROR
    }
}
