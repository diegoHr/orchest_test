package com.diego.hernando.orchestTest.business.weekReport.alarm;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
public class Alarm {

    private List<WorkSignDto> workSignsTriggeredAlarm;
    private String keyDescription;
    private Object[] descriptionParams;

    public AlarmDto getDto (MessageSource messageSource, Locale locale){
        return new AlarmDto(workSignsTriggeredAlarm,messageSource.getMessage(keyDescription, descriptionParams, locale));
    }


}
