package com.diego.hernando.orchestTest.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IJpaWorkSignRepository extends CrudRepository<WorkSignEntity, Long> {

    public List<WorkSignEntity> findByBusinessIdAndEmployeeIdOrderByDateAsc(String businessId, String employeeId);

    @Query(value = "FROM WorkSignEntity w WHERE w.date BETWEEN :startDate AND :endDate AND w.businessId = :businessId " +
            "AND w.employeeId = :employeeId ORDER BY w.date ASC")
    public List<WorkSignEntity> findEmployeeWorkSignBetweenTwoDates(@Param("businessId")String businessId,
                                                                    @Param("employeeId") String employeeId,
                                                                    @Param("startDate") Date startDate,
                                                                    @Param("endDate") Date finishDate);

}
