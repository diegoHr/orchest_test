package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import java.util.Locale;

public interface IAlarmParameterFormatter<F> {
    String write (Locale locale, F parameterToWrite);
}
