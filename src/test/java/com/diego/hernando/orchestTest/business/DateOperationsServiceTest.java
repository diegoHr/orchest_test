package com.diego.hernando.orchestTest.business;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static com.diego.hernando.orchestTest.testUtils.DefaultDateTimeFormatter.parseDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class DateOperationsServiceTest {

    @Spy
    private DateOperationsService dateOpService;

    @Test
    public void test_expected_init_week_from_fixed_present_in_several_weeks_transformWeekToInitWeekDate() {
        Mockito.when(dateOpService.getNow())
                .thenReturn(parseDateTime("05/05/2020 10:01:10").withMillisOfSecond(100));
        Date expectedInitWeek0Date = parseDateTime("04/05/2020 00:00:00").withMillisOfSecond(0).toDate();
        Date expectedInitWeek1Date = parseDateTime("27/04/2020 00:00:00").withMillisOfSecond(0).toDate();
        Date expectedInitWeek2Date = parseDateTime("20/04/2020 00:00:00").withMillisOfSecond(0).toDate();
        Date expectedInitWeek3Date = parseDateTime("11/05/2020 00:00:00").withMillisOfSecond(0).toDate();

        assertThat(dateOpService.transformWeekToInitWeekDate(0), is(expectedInitWeek0Date));
        assertThat(dateOpService.transformWeekToInitWeekDate(1), is(expectedInitWeek1Date));
        assertThat(dateOpService.transformWeekToInitWeekDate(2), is(expectedInitWeek2Date));
        assertThat(dateOpService.transformWeekToInitWeekDate(-1), is(expectedInitWeek3Date));
    }

    @Test
    public void test_expected_last_week_from_several_init_week_dates_getEndWeekDateFromInitWeekDate () {
        Date initDate1 = parseDateTime("04/05/2020 00:00:00").withMillisOfSecond(0).toDate();
        Date expectedLastDate1 = parseDateTime("10/05/2020 23:59:59").withMillisOfSecond(999).toDate();

        Date lastDate1 = dateOpService.getEndWeekDateFromInitWeekDate(initDate1);

        assertThat( lastDate1.getTime(), is(expectedLastDate1.getTime()));
        assertThat(lastDate1.getTime()+1,
                is(parseDateTime("11/05/2020 00:00:00").withMillisOfSecond(0).toDate().getTime()));

        Date initDate2 = parseDateTime("08/07/2020 00:00:00").withMillisOfSecond(0).toDate();
        Date expectedLastDate2 = parseDateTime("12/07/2020 23:59:59").withMillisOfSecond(999).toDate();
        assertThat( dateOpService.getEndWeekDateFromInitWeekDate(initDate2).getTime(),
                is(expectedLastDate2.getTime()));

    }

    @Test
    public void test_daysBetweenTwoDates () {
        Date initDate1 = parseDateTime("04/05/2020 10:10:30").withMillisOfSecond(133).toDate();
        Date initDate2 = parseDateTime("05/05/2020 00:10:30").withMillisOfSecond(133).toDate();
        assertThat(dateOpService.daysBetweenTwoDates(initDate1, initDate2), is(1));
        assertThat(dateOpService.daysBetweenTwoDates(initDate2, initDate1), is(1));

        initDate2 = parseDateTime("05/05/2020 23:59:59").toDate();
        assertThat(dateOpService.daysBetweenTwoDates(initDate1, initDate2), is(1));
        assertThat(dateOpService.daysBetweenTwoDates(initDate2, initDate1), is(1));
    }

    @Test
    public void test_isDateBetweenTwoDates () {
        DateTime date1 = parseDateTime("04/05/2020 10:10:30").withMillisOfSecond(133);
        DateTime date2 = parseDateTime("05/05/2020 00:10:30").withMillisOfSecond(133);
        Date date = parseDateTime("05/05/2020 00:09:30").withMillisOfSecond(133).toDate();

        assertThat(dateOpService.isDateBetweenTwoDates(date, date1, date2), is(true));
        assertThat(dateOpService.isDateBetweenTwoDates(date, date2, date1), is(true));

        date = parseDateTime("05/05/2020 10:09:30").withMillisOfSecond(133).toDate();
        assertThat(dateOpService.isDateBetweenTwoDates(date, date1, date2), is(false));
        assertThat(dateOpService.isDateBetweenTwoDates(date, date2, date1), is(false));

        date = parseDateTime("04/05/2020 10:09:30").withMillisOfSecond(133).toDate();
        assertThat(dateOpService.isDateBetweenTwoDates(date, date1, date2), is(false));
        assertThat(dateOpService.isDateBetweenTwoDates(date, date2, date1), is(false));

        date = date1.toDate();
        assertThat(dateOpService.isDateBetweenTwoDates(date, date1, date2), is(true));
        assertThat(dateOpService.isDateBetweenTwoDates(date, date2, date1), is(true));

        date = date2.toDate();
        assertThat(dateOpService.isDateBetweenTwoDates(date, date1, date2), is(true));
        assertThat(dateOpService.isDateBetweenTwoDates(date, date2, date1), is(true));

    }

}
