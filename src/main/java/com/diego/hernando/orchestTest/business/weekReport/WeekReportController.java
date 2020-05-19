package com.diego.hernando.orchestTest.business.weekReport;

import com.diego.hernando.orchestTest.business.restResponse.ErrorRestResponse;
import com.diego.hernando.orchestTest.business.restResponse.InternalErrorRestException;
import com.diego.hernando.orchestTest.business.weekReport.service.WeekReportGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.IllegalFieldValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
@RequestMapping("weekreports")
public class WeekReportController {

    private final MessageSource messageSource;
    private final WeekReportGeneratorService weekGenSrv;

    @Autowired
    public WeekReportController(MessageSource messageSource, WeekReportGeneratorService weekGenSrv) {
        this.messageSource = messageSource;
        this.weekGenSrv = weekGenSrv;
    }

    @GetMapping("/{businessId}/{employeeId}")
    public WeekReportDto getWeekReport (@PathVariable String businessId, @PathVariable String employeeId,
                                            @RequestParam(defaultValue = "0") String week,
                                           HttpServletRequest request){
        try {
            int weekInt = Integer.parseInt(week);
            return weekGenSrv.getWeekReport(weekInt,businessId, employeeId, request.getLocale());
        }catch(NumberFormatException th){
            throw getInternalErrorRestException(th, "weekreport.controller.get.week.format.error", request.getLocale());
        }catch (Throwable th){
            throw getInternalErrorRestException(th, "weekreport.controller.get.internal.error", request.getLocale());
        }
    }

    @GetMapping("/{businessId}/{employeeId}/{month}/{year}")
    public List<WeekReportDto> getWeekReport (@PathVariable String businessId, @PathVariable String employeeId,
                                              @PathVariable String month, @PathVariable String year,
                                              HttpServletRequest request){
        try {
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);
            return weekGenSrv.getWeekReports(monthInt, yearInt, businessId, employeeId, request.getLocale());
        }catch(NumberFormatException th) {
            throw getInternalErrorRestException(th, "weekreport.controller.get.date.format.error", request.getLocale());
        }catch (IllegalFieldValueException th){
            throw getInternalErrorRestException(th, "weekreport.controller.get.month.illegal.error", request.getLocale());
        }catch (Throwable th){
            throw getInternalErrorRestException(th, "weekreport.controller.get.internal.error", request.getLocale());
        }
    }

    private InternalErrorRestException getInternalErrorRestException (Throwable th, String keyMessage, Locale locale){
        return new InternalErrorRestException(
                messageSource.getMessage(keyMessage,null, locale),
                th);
    }

    @ExceptionHandler(InternalErrorRestException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorRestResponse handleInternalError (InternalErrorRestException e) {
        log.error(e.getMessage(),e);
        return ErrorRestResponse.builder().message(e.getMessage()).build();
    }


}
