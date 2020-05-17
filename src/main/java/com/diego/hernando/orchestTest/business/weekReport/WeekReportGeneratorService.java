package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.worksign.service.WorkSignReturnerByWeeksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeekReportGeneratorService {

    @Autowired
    private WorkSignReturnerByWeeksService workSignRetByWeeksSrv;

    public WeekReportDto getWeekReport (int week, String businessId, String employeeId){

        return null;
    }

}
