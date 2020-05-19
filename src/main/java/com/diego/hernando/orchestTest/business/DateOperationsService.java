package com.diego.hernando.orchestTest.business;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class DateOperationsService {

    @SuppressWarnings("FieldCanBeLocal")
    private final long DURATION_DAY_MILLISECONDS = 24*60*60*1000;

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

    public Date getFinishNextDay (Date date){
        return new DateTime(date).plusDays(1).withHourOfDay(23)
                .withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).toDate();
    }

    public Date getInitDayFromDate (Date date){
        return getInitDayDateTimeFromDate(date).toDate();
    }

    public DateTime getInitDayDateTimeFromDate (Date date) {
        return new DateTime(date).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
    }

    public Date getFinishDayFromDate (Date date){
        return new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).toDate();
    }

    public int daysBetweenTwoDates (Date date1, Date date2){
        return Long.valueOf(Math.abs(getInitDayFromDate(date1).getTime() - getInitDayFromDate(date2).getTime()) / DURATION_DAY_MILLISECONDS)
                .intValue();
    }

    public boolean isDateBetweenTwoDates(Date date, DateTime date1, DateTime date2){
        return date1.isBefore(date.getTime()) && date2.isAfter(date.getTime()) || date1.isAfter(date.getTime()) && date2.isBefore(date.getTime())
                || date1.isEqual(date.getTime()) || date2.isEqual(date.getTime()) ;
    }

    public List<Date> getInitWeeks(int month, int year) {
        List<Date> initWeeks = new LinkedList<>();
        int nextMonth = month+1;
        int nextYear = year;
        if(month == 12){
            nextMonth = 1;
            nextYear = year+1;
        }
        Date lastMonthDay = getInitDayFromDate(DateTime.now().withDayOfMonth(1).withYear(nextYear).withMonthOfYear(nextMonth).minusDays(1).toDate());
        DateTime initWeek = getInitDayDateTimeFromDate(DateTime.now().withYear(year).withMonthOfYear(month).withDayOfMonth(1).withDayOfWeek(1).toDate());

        while(initWeek.isBefore(lastMonthDay.getTime())){
            initWeeks.add(initWeek.toDate());
            initWeek = initWeek.plusDays(7);
        }

        return  initWeeks;
    }
}
