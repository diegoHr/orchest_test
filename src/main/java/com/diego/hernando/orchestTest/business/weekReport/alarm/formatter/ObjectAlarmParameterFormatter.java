package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ObjectAlarmParameterFormatter implements IAlarmParameterFormatter<Object> {

    @Override
    public String write(Locale locale, Object parameterToWrite) {
        return parameterToWrite.toString();
    }
}
