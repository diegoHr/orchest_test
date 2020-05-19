package com.diego.hernando.orchestTest.business.worksign;

import com.diego.hernando.orchestTest.business.restResponse.InternalErrorRestException;
import com.diego.hernando.orchestTest.business.restResponse.ErrorRestResponse;
import com.diego.hernando.orchestTest.business.restResponse.IRestResponse;
import com.diego.hernando.orchestTest.business.restResponse.OkRestResponse;
import com.diego.hernando.orchestTest.business.worksign.service.transformWorkSignService.ITransformJsonCrudWorkSignService;
import com.diego.hernando.orchestTest.model.service.ICrudWorkSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("worksigns")
public class WorkSignsController {


    private final ITransformJsonCrudWorkSignService transformJsonCrudService;


    private final ICrudWorkSignService crudService;

    private final MessageSource messageSource;

    @Autowired
    public WorkSignsController(@Qualifier("TransformJsonCrudWorkingSignService") ITransformJsonCrudWorkSignService transformJsonCrudService,
                               @Qualifier("CrudJpaWorkSignService") ICrudWorkSignService crudService,
                               MessageSource messageSource) {
        this.transformJsonCrudService = transformJsonCrudService;
        this.crudService = crudService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public IRestResponse saveWorkSigns (HttpServletRequest request, HttpServletResponse response, @RequestBody List<WorkSignDto> workSigns){
        try {
            int size = transformJsonCrudService.getListEntitiesSaved(workSigns).size();
            return OkRestResponse.builder().message(
                    messageSource.getMessage("worksigns.controller.save.success", new Integer[]{size}, request.getLocale())).build();
        }catch (Throwable th){
            String errorMessage = messageSource.getMessage("worksigns.controller.save.error", null,
                    request.getLocale());
            log.error(errorMessage,th);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ErrorRestResponse.builder().message(errorMessage).build();
        }
    }

    @GetMapping("/{businessid}/{employeeId}")
    public List<WorkSignDto> getWorkSigns (@PathVariable("businessid") String businessId, @PathVariable String employeeId,
                                           HttpServletRequest request){
        try {
            return transformJsonCrudService.getListDto(crudService.findWorkSignsOfWorker(businessId, employeeId));
        }catch(Throwable th){
            throw new InternalErrorRestException(
                    messageSource.getMessage("worksings.controller.get.persistence.error",null, request.getLocale()),
                    th);
        }
    }

    @ExceptionHandler(InternalErrorRestException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorRestResponse handleInternalError (InternalErrorRestException e) {
        log.error(e.getMessage(),e);
        return ErrorRestResponse.builder().message(e.getMessage()).build();
    }
}
