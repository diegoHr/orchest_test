package com.diego.hernando.orchestTest.business.restResponse;

public class InternalErrorRestException extends RuntimeException{
    public InternalErrorRestException(String message, Throwable cause){
        super(message,cause);
    }
}
