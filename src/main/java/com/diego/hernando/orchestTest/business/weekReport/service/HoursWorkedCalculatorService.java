package com.diego.hernando.orchestTest.business.weekReport.service;

import com.diego.hernando.orchestTest.business.DateOperationsService;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class HoursWorkedCalculatorService {

    private final WorkSignOperationsService wSignOpSrv;
    private final DateOperationsService dateOpSrv;
    private final double AN_HOUR_IN_MILLISECONDS = 60*60*1000;

    @Autowired
    public HoursWorkedCalculatorService(WorkSignOperationsService wSignOpSrv, DateOperationsService dateOpSrv) {
        this.wSignOpSrv = wSignOpSrv;
        this.dateOpSrv = dateOpSrv;
    }

    public double calculateHoursWorked (List<WorkSignDto> wSigns) {
        return calculateHoursWithFilteredWSigns(wSigns, wSignOpSrv::isWorkTypeWorkSign)
                - calculateHoursWithFilteredWSigns(wSigns, wSignOpSrv::isRestTypeWorkSign);
    }

    protected double calculateHoursWithFilteredWSigns (List<WorkSignDto> wSigns, Predicate<WorkSignDto> predicate) {
         return calculateHours(wSigns.stream().filter(predicate)
                .collect(Collectors.toList()));
    }

    protected double calculateHours (List<WorkSignDto> wSigns){
        double hours = 0;

        List<WorkSignDto> copyWSigns = new ArrayList<>(wSigns);
        if(wSigns.size() > 0 && wSignOpSrv.isOutRecordTypeWSign(copyWSigns.get(0))){
            hours += (copyWSigns.get(0).getDate().getTime()
                    - dateOpSrv.getInitDayFromDate(copyWSigns.get(0).getDate()).getTime())/AN_HOUR_IN_MILLISECONDS;
            copyWSigns.remove(0);
        }
        if(copyWSigns.size() > 0 && wSignOpSrv.isInRecordTypeWSign(copyWSigns.get(copyWSigns.size() - 1))){
            Date lastWSignDate = copyWSigns.get(copyWSigns.size() -1).getDate();
            hours += (dateOpSrv.getFinishDayFromDate(lastWSignDate).getTime() - lastWSignDate.getTime() )/AN_HOUR_IN_MILLISECONDS;
            copyWSigns.remove(copyWSigns.size() - 1);
        }

        if(copyWSigns.size() == 0){
            return hours;
        }else{
            return hours + simpleCalculateHours(copyWSigns);
        }
    }

    protected double simpleCalculateHours(List<WorkSignDto> wSigns){
        double hours = 0;
        for(int i = 0; i+1 < wSigns.size(); i +=2){
            WorkSignDto in = wSigns.get(i);
            WorkSignDto out = wSigns.get(i+1);
            if(wSignOpSrv.isInRecordTypeWSign(in) && wSignOpSrv.isOutRecordTypeWSign(out)){
                hours += (out.getDate().getTime() - in.getDate().getTime())/AN_HOUR_IN_MILLISECONDS;
            }
        }
        return hours;
    }


}
