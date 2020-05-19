package com.diego.hernando.orchestTest.model.service;

import com.diego.hernando.orchestTest.model.WorkSignEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICrudWorkSignService {

    Optional<WorkSignEntity> findById(long id);
    WorkSignEntity save(WorkSignEntity entity);
    List<WorkSignEntity> saveAll(List<WorkSignEntity> collection);
    long count();
    List<WorkSignEntity> findWorkSignsOfWorker(String businessId, String employeeId);
    List<WorkSignEntity> findEmployeeWorkSignsBetweenTwoDates(String businessId, String employeeId, Date startDate, Date finishDate);
}
