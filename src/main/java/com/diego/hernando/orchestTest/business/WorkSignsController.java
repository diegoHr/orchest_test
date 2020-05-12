package com.diego.hernando.orchestTest.business;

import com.diego.hernando.orchestTest.business.restResponse.ErrorRestResponse;
import com.diego.hernando.orchestTest.business.restResponse.IRestResponse;
import com.diego.hernando.orchestTest.business.restResponse.OkRestResponse;
import com.diego.hernando.orchestTest.business.service.ITransformJsonCrudWorkSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("worksigns")
public class WorkSignsController {

    @Autowired
    @Qualifier("TransformJsonCrudWorkingSignService")
    private ITransformJsonCrudWorkSignService transformJsonCrudService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping
    public IRestResponse saveWorkSigns (HttpServletRequest request, @RequestBody List<WorkSignDto> workSigns){
        try {
            Integer size = transformJsonCrudService.getListEntitiesSaved(workSigns).size();
            return OkRestResponse.builder().message(
                    messageSource.getMessage("worksigns.controller.save.success", new Integer[]{size}, request.getLocale())).build();
        }catch (Throwable th){
            String errorMessage = messageSource.getMessage("worksigns.controller.save.error", null,
                    request.getLocale());
            log.error(errorMessage,th);
            return ErrorRestResponse.builder().message(errorMessage).build();
        }
    }
}
