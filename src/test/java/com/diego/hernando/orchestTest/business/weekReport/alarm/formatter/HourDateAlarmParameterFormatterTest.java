package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import org.junit.jupiter.api.Test;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HourDateAlarmParameterFormatterTest {

    private final HourDateAlarmParameterFormatter hourDateFormatter = new HourDateAlarmParameterFormatter();

    @Test
    public void test () {
        assertThat(hourDateFormatter.write(null, parseDate("04/05/2020 08:00:00")),is("08:00"));
    }
}
