package com.diego.hernando.orchestTest.business.restResponse;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface IRestResponse {

    public Status getStatus ();
    public String getMessage ();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public static enum Status {
        OK,ERROR
    }
}
