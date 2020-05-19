package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import java.util.Locale;

public interface IAlarmParameterFormatter<F,R> {
    R write (Locale locale, F parameterToWrite);
}
