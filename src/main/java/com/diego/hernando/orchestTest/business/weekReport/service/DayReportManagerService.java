package com.diego.hernando.orchestTest.business.weekReport.service;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.DayReportDto;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DayReportManagerService {

    private final DateOperationsService dateOpSrv;

    @Autowired
    public DayReportManagerService(DateOperationsService dateOpSrv) {
        this.dateOpSrv = dateOpSrv;
    }

    public List<DayReportDto> getDayReportsFromWSignsOfWeek (List<WorkSignDto> weekWsigns){
        if(weekWsigns.size() == 0){
            return new ArrayList<>();
        }
        Date initDate = weekWsigns.get(0).getDate();
        Date finishDate = weekWsigns.get(weekWsigns.size()-1).getDate();
        int days = dateOpSrv.daysBetweenTwoDates(initDate, finishDate) + 1 ;
        List<DayReportDto> dayReportDtos = new ArrayList<>(days);
        DateTime finishDay = new DateTime(dateOpSrv.getFinishDayFromDate(initDate));
        DateTime initDay = new DateTime(dateOpSrv.getInitDayFromDate(initDate));

        for(int i = 0; i<days; i++){
            int finalI = i;
            List<WorkSignDto> dayWSigns = weekWsigns.stream().filter(wSign ->
                    dateOpSrv.isDateBetweenTwoDates(wSign.getDate(),
                            initDay.plusDays(finalI),
                            finishDay.plusDays(finalI))
            ).collect(Collectors.toList());

            if(dayWSigns.size() > 0){
                dayReportDtos.add(new DayReportDto(initDay.plusDays(i).toDate(), dayWSigns));
            }
        }
        return dayReportDtos;
    }

}
