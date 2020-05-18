package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlarmParameterFormattersFactoryServiceTest {

    @Autowired
    private AlarmParameterFormattersFactoryService factory;

    @Test
    public void test() {
        Locale esLocale = new Locale.Builder().setLanguage("es").build();

        assertThat(factory.getAlarmParameterFormatter(PrettyPrintDateAlarmParameterFormatter.class)
                        .write(esLocale, parseDate("04/05/2020 08:00:00")),
                is("lun, may 4, 2020"));
    }
}
