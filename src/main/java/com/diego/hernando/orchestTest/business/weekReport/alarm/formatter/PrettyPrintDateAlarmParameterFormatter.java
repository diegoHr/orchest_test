package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import com.diego.hernando.orchestTest.configuration.Constants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Locale;

public class PrettyPrintDateAlarmParameterFormatter  implements IAlarmParameterFormatter <Date>{

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(Constants.PATTERN_DATE_PRETTY_PRINT_JODA);


    @Override
    public String write(Locale locale, Date parameterToWrite) {
        return dateTimeFormatter.withLocale(locale).print(parameterToWrite.getTime());
    }
}
