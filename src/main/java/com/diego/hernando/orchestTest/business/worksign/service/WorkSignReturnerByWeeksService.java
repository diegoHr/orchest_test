package com.diego.hernando.orchestTest.business.worksign.service;

import com.diego.hernando.orchestTest.business.worksign.WorkSignDto;
import com.diego.hernando.orchestTest.business.worksign.service.transformWorkSignService.ITransformJsonCrudWorkSignService;
import com.diego.hernando.orchestTest.model.service.ICrudWorkSignService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkSignReturnerByWeeksService {

    private final ICrudWorkSignService crudWSignService;

    private final ITransformJsonCrudWorkSignService transJsonCrudWSignSrv;

    private final WorkSignOperationsService wSignOpSrv;

    @Autowired
    public WorkSignReturnerByWeeksService(@Qualifier("CrudJpaWorkSignService") ICrudWorkSignService crudWSignService,
                                          @Qualifier("TransformJsonCrudWorkingSignService")
                                                    ITransformJsonCrudWorkSignService transJsonCrudWSignSrv,
                                          WorkSignOperationsService wSignOpSrv) {
        this.crudWSignService = crudWSignService;
        this.transJsonCrudWSignSrv = transJsonCrudWSignSrv;
        this.wSignOpSrv = wSignOpSrv;
    }
    protected DateTime getNow () {
        return DateTime.now();
    }

    public Date transformWeekToInitWeekDate (int week){
        return getNow().minusWeeks(week).withDayOfWeek(1).withHourOfDay(0)
                .withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
    }

    public Date getEndWeekDateFromInitWeekDate(Date initDateWeek){
        return new DateTime(initDateWeek).withDayOfWeek(7).withHourOfDay(23)
                .withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).toDate();
    }

    public Date getInitPreviousDate (Date initWeekDate){
        return new DateTime(initWeekDate).minusDays(1).toDate();
    }

    public List<WorkSignDto> getEmployeeWSignsOfWeek (String businessId, String employeeId, int week){
        return getEmployeeWSingsOfWeek(businessId, employeeId, transformWeekToInitWeekDate(week));
    }

    protected List<WorkSignDto> getEmployeeWSingsOfWeek(String businessId, String employeeId, Date initWeek){
        Date endWeekDate = getEndWeekDateFromInitWeekDate(initWeek);
        List<WorkSignDto> wSigns = transJsonCrudWSignSrv.getListDto(
                crudWSignService.findEmployeeWorkSignsBetweenTwoDates(
                        businessId,
                        employeeId,
                        initWeek,
                        endWeekDate
                        )
        );
        if(!isFreeWorkInitWeek(wSigns)){
            wSigns.addAll(0, getIncompleteWSignsOfPreviousDayOfWeek(businessId, employeeId, initWeek));
        }
        return deleteIncompleteEndWeekWSigns(wSigns,endWeekDate);
    }

    protected Optional<WorkSignDto> getLastWorkInDayWsign (List<WorkSignDto> wSigns){
        return wSigns.stream().filter(wSignOpSrv::isInRecordTypeAndWorkTypeWSign)
                .reduce((first, second) -> second);
    }



    protected List<WorkSignDto> getIncompleteWSignsOfPreviousDayOfWeek(String businessId, String employeeId, Date initWeek){
        List<WorkSignDto> wSignsPreviousDay =  transJsonCrudWSignSrv.getListDto(
                crudWSignService.findEmployeeWorkSignsBetweenTwoDates(
                        businessId,
                        employeeId,
                        getInitPreviousDate(initWeek),
                        new Date(initWeek.getTime()-1))
        );
        return getIncompleteWSignsOfDay(wSignsPreviousDay);
    }

    protected List<WorkSignDto> getLastDayWeekWsigns(List<WorkSignDto> wSigns, Date lastDayDate){
        if(wSigns.size() > 0) {
            Date initLastDay = new DateTime(lastDayDate)
                    .withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
                    .withMillisOfSecond(0).toDate();
            return wSigns.stream().filter(wSign-> wSign.getDate().getTime() >= initLastDay.getTime()).collect(Collectors.toList());
        }else{
            return wSigns;
        }

    }

    protected List<WorkSignDto> getIncompleteWSignsOfDay(List<WorkSignDto> wSignsDay){
        Optional <WorkSignDto> optLastWorkInDayWSign = getLastWorkInDayWsign(wSignsDay);
        List<WorkSignDto> incompleteWsignsWithoutVerify = optLastWorkInDayWSign.map(workSignDto ->
                wSignsDay.subList(wSignsDay.indexOf(workSignDto), wSignsDay.size())
        ).orElseGet(ArrayList::new);

        boolean lastWorkInDayFoundedIsComplete = incompleteWsignsWithoutVerify.stream()
                .anyMatch(wSignOpSrv::isOutRecordTypeAndWorkTypeWSign);
        return !lastWorkInDayFoundedIsComplete ? incompleteWsignsWithoutVerify : new ArrayList<>();
    }

    protected boolean isFreeWorkInitWeek(List<WorkSignDto> wSignsOfWeek){
        return  wSignsOfWeek.size() > 0 && wSignOpSrv.isInRecordTypeAndWorkTypeWSign(wSignsOfWeek.get(0));
    }

    protected List<WorkSignDto> deleteIncompleteEndWeekWSigns(List<WorkSignDto> weekWSigns, Date endWeekDate){
        weekWSigns.removeAll(getIncompleteWSignsOfDay(
                getLastDayWeekWsigns(weekWSigns, endWeekDate)
        ));
        return weekWSigns;
    }
}
