package com.diego.hernando.orchestTest.model.service;

import com.diego.hernando.orchestTest.model.WorkSignEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICrudWorkSignService {

    public Optional<WorkSignEntity> findById(long id);
    public WorkSignEntity save(WorkSignEntity entity);
    public List<WorkSignEntity> saveAll(List<WorkSignEntity> collection);
    public long count();
    public List<WorkSignEntity> findWorkSignsOfWorker(String businessId, String employeeId);
    public List<WorkSignEntity> findEmployeeWorkSignsBetweenTwoDates(String businessId, String employeeId, Date startDate, Date finishDate);
}
