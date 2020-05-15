package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import lombok.Data;

import java.util.List;

@Data
public class DayReportDto {

    private List<WorkSignDto> dayWorkSigns;

}
