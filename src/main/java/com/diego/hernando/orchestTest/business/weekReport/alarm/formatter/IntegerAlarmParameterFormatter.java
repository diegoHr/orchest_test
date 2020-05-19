package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class IntegerAlarmParameterFormatter implements IAlarmParameterFormatter<Integer, Integer> {

    @Override
    public Integer write(Locale locale, Integer parameterToWrite) {
        return parameterToWrite;
    }
}
