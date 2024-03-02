package com.example.persistence.entity;

import com.example.util.AbstractJsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


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
