package com.diego.hernando.orchestTest.business;

import com.diego.hernando.orchestTest.business.restResponse.IRestResponse;
import com.diego.hernando.orchestTest.business.restResponse.OkRestResponse;
import com.diego.hernando.orchestTest.business.service.ITransformJsonCrudWorkSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("worksigns")
public class WorkSignsController {

    @Autowired
    private ITransformJsonCrudWorkSignService transformJsonCrudService;

    @PostMapping
    public IRestResponse saveWorkSigns (@RequestBody List<WorkSignDto> workSigns){
        return OkRestResponse.builder().build();
    }
}
