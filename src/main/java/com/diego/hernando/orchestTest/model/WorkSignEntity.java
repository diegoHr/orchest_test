package com.diego.hernando.orchestTest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {@Index(name = "dateOrderedIndex",  columnList="date")})
public class WorkSignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String businessId;

    @Column(nullable=false)
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
