package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import java.util.Locale;

public class ObjectAlarmParameterFormatter implements IAlarmParameterFormatter<Object> {

    @Override
    public String write(Locale locale, Object parameterToWrite) {
        return parameterToWrite.toString();
    }


}
