package com.diego.hernando.orchestTest.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJpaWorkSignRepository extends CrudRepository<WorkSignEntity, Long> {

    public List<WorkSignEntity> findByBusinessIdAndEmployeeIdOrderByDateAsc(String businessId, String employeeId);
}
