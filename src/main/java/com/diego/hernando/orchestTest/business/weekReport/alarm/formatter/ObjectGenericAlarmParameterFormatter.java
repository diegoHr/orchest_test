package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import java.util.Locale;

public class ObjectGenericAlarmParameterFormatter <F> implements IAlarmParameterFormatter<F>{

    @Override
    public String write(Locale locale, F parameterToWrite) {
        return parameterToWrite.toString();
    }
}
