package com.diego.hernando.orchestTest.business.weekReport.alarm.checker.weekly;

import com.diego.hernando.orchestTest.business.weekReport.alarm.Alarm;
import com.diego.hernando.orchestTest.business.weekReport.alarm.AlarmLevel;
import com.diego.hernando.orchestTest.business.weekReport.alarm.checker.helper.IncompleteWSignsOperationsService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.AlarmParameterFormattersFactoryService;
import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.IAlarmParameterFormatter;
import com.diego.hernando.orchestTest.business.weekReport.alarm.formatter.ObjectAlarmParameterFormatter;
import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.WorkSignOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class BadRestWSignAlarmChecker implements IWeeklyAlarmCheckerService {

    private final WorkSignOperationsService workSignOpSrv;
    private final IncompleteWSignsOperationsService incompWSignsOpSrv;
    private final AlarmParameterFormattersFactoryService alarmParameterFormattersFactory;

    @Autowired
    public BadRestWSignAlarmChecker(WorkSignOperationsService workSignOpSrv,
                                    IncompleteWSignsOperationsService incompWSignsOpSrv,
                                    AlarmParameterFormattersFactoryService alarmParameterFormattersFactory) {
        this.workSignOpSrv = workSignOpSrv;
        this.incompWSignsOpSrv = incompWSignsOpSrv;
        this.alarmParameterFormattersFactory = alarmParameterFormattersFactory;
    }

    @Override
    public String getKeyDescription() {
        return "alarm.checker.bad.rest.worksign";
    }

    @Override
    public AlarmLevel getLevel() {
        return AlarmLevel.ERROR;
    }

    @Override
    public List<IAlarmParameterFormatter<Object>> getParameterFormatters() {
        return Collections.singletonList(alarmParameterFormattersFactory.getAlarmParameterFormatter(ObjectAlarmParameterFormatter.class));
    }

    @Override
    public List<Alarm> check(List<WorkSignDto> wSignsToCheck) {
        List<WorkSignDto> restWsigns = wSignsToCheck.stream().filter(workSignOpSrv::isRestTypeWorkSign)
                .collect(Collectors.toList());

        List<WorkSignDto> wSignsTrigeredAlarm = incompWSignsOpSrv.extractIncompleteWSigns(restWsigns);

        List<WorkSignDto> wSignsApparentlyOk = restWsigns.stream()
                .filter(wSign -> !wSignsTrigeredAlarm.contains(wSign)).collect(Collectors.toList());

        List<Alarm> alarmsWSignsOutWork = getRestWSignsOutWorkAlarms(wSignsApparentlyOk, getWorkWSignsOk(wSignsToCheck));
        if(wSignsTrigeredAlarm.size() > 0){
            return Stream.concat(
                    Stream.of(createAlarm(wSignsTrigeredAlarm)),
                    alarmsWSignsOutWork.stream()).collect(Collectors.toList());
        }else{
            return alarmsWSignsOutWork;
        }
    }

    private List<Alarm> getRestWSignsOutWorkAlarms (List<WorkSignDto> restWsigns, List<WorkSignDto> workSignDtos) {
        List<Alarm> alarms = new LinkedList<>();
        List<WorkSignDto> copyWorkSignDtos = new ArrayList<>(workSignDtos);
        for(int i = 0; i < restWsigns.size()-1 ; i+=2){
            WorkSignDto initRestWSign = restWsigns.get(i);
            WorkSignDto finishRestWSign = restWsigns.get(i+1);

            int indexInitDel = -1;

            for(int o = 0; o < copyWorkSignDtos.size()-1; o+=2){
                WorkSignDto workWSignI = copyWorkSignDtos.get(o);
                WorkSignDto workWSignII = copyWorkSignDtos.get(o+1);

                if(!(isRestWSignInWork(initRestWSign, workWSignI, workWSignII)
                        && isRestWSignInWork(finishRestWSign, workWSignI, workWSignII))){
                    if(!(initRestWSign.getDate().getTime() > workWSignI.getDate().getTime() && finishRestWSign.getDate().getTime() > workWSignII.getDate().getTime())){
                        indexInitDel = o-1;
                        alarms.add(createAlarm(Arrays.asList(initRestWSign,finishRestWSign)));
                        break;
                    }
                }else{
                    indexInitDel = o-1;
                    break;
                }
            }
            if(indexInitDel >= 0){
                int finalIndexInitDel = indexInitDel;
                IntStream.rangeClosed(0, indexInitDel).map(index -> finalIndexInitDel - index).forEach(copyWorkSignDtos::remove);
            }
        }
        return alarms;
    }

    private Alarm createAlarm (List<WorkSignDto> wSignsTriggeredAlarm){
        return new Alarm(wSignsTriggeredAlarm, getKeyDescription(), new Object[]{wSignsTriggeredAlarm.size()}, getParameterFormatters(), getLevel());
    }

    private boolean isRestWSignInWork (WorkSignDto restWSign, WorkSignDto workWSignI, WorkSignDto workWSignII){
        return workSignOpSrv.isInRecordTypeWSign(workWSignI)
                && workSignOpSrv.isOutRecordTypeWSign(workWSignII)
                && workWSignII.getDate().getTime() > restWSign.getDate().getTime()
                && workWSignI.getDate().getTime() < restWSign.getDate().getTime();
    }

    private List<WorkSignDto> getWorkWSignsOk (List<WorkSignDto> wSigns){
        List<WorkSignDto> workWSigns = wSigns.stream().filter(workSignOpSrv::isWorkTypeWorkSign)
                .collect(Collectors.toList());
        Set<WorkSignDto> workWsignsTriggeredAlarm = new HashSet<>(incompWSignsOpSrv.extractIncompleteWSigns(workWSigns));
        return workWSigns.stream()
                .filter(wSign -> !workWsignsTriggeredAlarm.contains(wSign)).collect(Collectors.toList());
    }
}
