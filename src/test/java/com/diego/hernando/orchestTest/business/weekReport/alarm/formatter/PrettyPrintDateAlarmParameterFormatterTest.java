package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PrettyPrintDateAlarmParameterFormatterTest {
    private final PrettyPrintDateAlarmParameterFormatter prettyPrintDateFormatter =
            new PrettyPrintDateAlarmParameterFormatter();

    @Test
    public void test () {
        Locale esLocale = new Locale.Builder().setLanguage("es").build();
        assertThat(prettyPrintDateFormatter.write(esLocale, parseDate("04/05/2020 08:00:00")),is("lun, may 4, 2020"));

        Locale enLocale = new Locale.Builder().setLanguage("en").build();
        assertThat(prettyPrintDateFormatter.write(enLocale, parseDate("04/05/2020 08:00:00")),is("Mon, May 4, 2020"));
    }
}
