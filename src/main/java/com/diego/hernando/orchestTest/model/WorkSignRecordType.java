package com.diego.hernando.orchestTest.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum WorkSignRecordType {
    IN,OUT
}
