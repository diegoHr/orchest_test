package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import com.diego.hernando.orchestTest.configuration.Constants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Locale;

@Component
public class HourDateAlarmParameterFormatter implements IAlarmParameterFormatter<Date, String>{

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(Constants.PATTERN_SIMPLE_HOUR_JODA);

    @Override
    public String write(Locale locale, Date parameterToWrite) {
        return dateTimeFormatter.print(parameterToWrite.getTime());
    }
}
