package com.diego.hernando.orchestTest.business.weekReport.alarm.formatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmParameterFormattersFactoryService {

    private Map<String, IAlarmParameterFormatter<Object,Object> > mapFormatters;

    @SuppressWarnings("unchecked")
    @Autowired
    void setAlarmParameterFormatters (List<IAlarmParameterFormatter<?,?>> formatters){
        mapFormatters = new HashMap<>();
        formatters.forEach(formatter -> mapFormatters.put(getKey(formatter.getClass()),
                (IAlarmParameterFormatter<Object, Object>) formatter));
    }

    @SuppressWarnings("rawtypes")
    private String getKey(Class<? extends IAlarmParameterFormatter> classFormatter){
        return classFormatter.getName();
    }

    public  IAlarmParameterFormatter<Object, Object> getAlarmParameterFormatter (Class<? extends IAlarmParameterFormatter<?,?>> classFormatter){
        return mapFormatters.containsKey(getKey(classFormatter))
                ? mapFormatters.get(getKey(classFormatter))
                : new ObjectGenericAlarmParameterFormatter<>();
    }
}
