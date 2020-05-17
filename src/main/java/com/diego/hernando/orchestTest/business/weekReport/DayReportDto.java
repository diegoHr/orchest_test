package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class DayReportDto {
    private Date initDay;
    private List<WorkSignDto> dayWorkSigns;
}
