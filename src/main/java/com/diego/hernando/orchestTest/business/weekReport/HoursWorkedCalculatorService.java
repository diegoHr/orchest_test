package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class HoursWorkedCalculatorService {

    private final WorkSignOperationsService wSignOpSrv;

    @Autowired
    public HoursWorkedCalculatorService(WorkSignOperationsService wSignOpSrv) {
        this.wSignOpSrv = wSignOpSrv;
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
        double anHourInMilliseconds = 60*60*1000;
        double hours = 0;
        for(int i = 0; i+1 < wSigns.size(); i +=2){
            WorkSignDto in = wSigns.get(i);
            WorkSignDto out = wSigns.get(i+1);
            if(wSignOpSrv.isInRecordTypeWSign(in) && wSignOpSrv.isOutRecordTypeWSign(out)){
                hours += (out.getDate().getTime() - in.getDate().getTime())/anHourInMilliseconds;
            }
        }
        return hours;
    }


}
