package com.diego.hernando.orchestTest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DefaultDateTimeFormatter {

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

    public static Date parseDate (String date){
        return parseDateTime(date).toDate();
    }

    public static DateTime parseDateTime (String date){
        return formatter.parseDateTime(date);
    }





}
