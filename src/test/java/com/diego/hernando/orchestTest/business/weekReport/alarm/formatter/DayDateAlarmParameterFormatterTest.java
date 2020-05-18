package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DayDateAlarmParameterFormatterTest {

    private final DayDateAlarmParameterFormatter dayDateFormatter =
            new DayDateAlarmParameterFormatter();

    @Test
    public void test () {
        Locale esLocale = new Locale.Builder().setLanguage("es").build();
        assertThat(dayDateFormatter.write(esLocale, parseDate("04/05/2020 08:00:00")),is("lunes"));
        assertThat(dayDateFormatter.write(esLocale, parseDate("05/05/2020 08:00:00")),is("martes"));
        assertThat(dayDateFormatter.write(esLocale, parseDate("06/05/2020 08:00:00")),is("miércoles"));
        assertThat(dayDateFormatter.write(esLocale, parseDate("07/05/2020 08:00:00")),is("jueves"));
        assertThat(dayDateFormatter.write(esLocale, parseDate("08/05/2020 08:00:00")),is("viernes"));
        assertThat(dayDateFormatter.write(esLocale, parseDate("09/05/2020 08:00:00")),is("sábado"));
        assertThat(dayDateFormatter.write(esLocale, parseDate("10/05/2020 08:00:00")),is("domingo"));

        Locale enLocale = new Locale.Builder().setLanguage("en").build();
        assertThat(dayDateFormatter.write(enLocale, parseDate("04/05/2020 08:00:00")),is("Monday"));
    }
}
