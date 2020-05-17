package com.diego.hernando.orchestTest.business;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DateOperationsService {

    public DateTime getNow () {
        return DateTime.now();
    }

    public Date transformWeekToInitWeekDate (int week){
        return getNow().minusWeeks(week).withDayOfWeek(1).withHourOfDay(0)
                .withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
    }

    public Date getEndWeekDateFromInitWeekDate(Date initDateWeek){
        return new DateTime(initDateWeek).withDayOfWeek(7).withHourOfDay(23)
                .withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).toDate();
    }

    public Date getInitPreviousDate (Date initWeekDate){
        return new DateTime(initWeekDate).minusDays(1).withHourOfDay(0)
                .withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
    }

}
