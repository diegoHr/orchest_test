package com.diego.hernando.orchestTest.business.weekReport.alarm;

import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.IAlarmParameterFormatter;
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
    private List<IAlarmParameterFormatter<Object>> formatters;
    private AlarmLevel alarmLevel;

    public AlarmDto getDto (MessageSource messageSource, Locale locale){
        return new AlarmDto(workSignsTriggeredAlarm,messageSource.getMessage(keyDescription,
                getDescriptionParamsFormatted(locale), locale), alarmLevel);
    }

    private Object[] getDescriptionParamsFormatted (Locale locale){
        Object[] descriptionParamsFormatted = new Object[descriptionParams.length];
        for(int i = 0; i < descriptionParams.length; i++){
            descriptionParamsFormatted[i] = formatters.get(i).write(locale, descriptionParams[i]);
        }
        return descriptionParamsFormatted;
    }

}
