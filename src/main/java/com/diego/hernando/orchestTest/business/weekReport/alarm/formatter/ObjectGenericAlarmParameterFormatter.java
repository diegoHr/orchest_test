package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import java.util.Locale;

public class ObjectGenericAlarmParameterFormatter <F> implements IAlarmParameterFormatter<F, Object>{

    @Override
    public Object write(Locale locale, F parameterToWrite) {
        return parameterToWrite.toString();
    }
}
