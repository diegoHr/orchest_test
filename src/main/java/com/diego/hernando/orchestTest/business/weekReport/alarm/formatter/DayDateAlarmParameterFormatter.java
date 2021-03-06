package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import com.diego.hernando.orchestTest.configuration.Constants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

@Component
public class DayDateAlarmParameterFormatter implements IAlarmParameterFormatter<Date, String> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(Constants.PATTERN_DAY_JODA);

    @Override
    public String write(Locale locale, Date parameterToWrite) {
        return dateTimeFormatter.withLocale(locale).print(parameterToWrite.getTime());
    }
}
