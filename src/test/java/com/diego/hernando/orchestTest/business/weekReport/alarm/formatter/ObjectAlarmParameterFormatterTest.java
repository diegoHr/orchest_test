package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ObjectAlarmParameterFormatterTest {
    private final ObjectAlarmParameterFormatter objectFormatter = new ObjectAlarmParameterFormatter();

    @Test
    public void test(){
        assertThat(objectFormatter.write(null, "Hello world"),is("Hello world"));
    }
}
