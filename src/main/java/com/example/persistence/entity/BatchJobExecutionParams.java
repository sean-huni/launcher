package com.example.persistence.entity;

import com.example.util.AbstractJsonUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class BatchJobExecutionParams extends AbstractJsonUtil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_execution_id", nullable = false)
    private Long jobExecutionId;
    @Column(name = "string_val", nullable = false)
    private String stringVal;
    @OneToOne
    @MapsId
    @JoinColumn(name = "job_execution_id")
    private BatchJobExecution batchJobExecution;
}
