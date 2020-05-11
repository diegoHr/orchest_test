package com.diego.hernando.orchestTest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class WorkSignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String businessId;

    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable=false)
    private String employeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length = 6)
    private WorkSignRecordType recordType;


    private String serviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length = 6)
    private WorkSignType type;

}
