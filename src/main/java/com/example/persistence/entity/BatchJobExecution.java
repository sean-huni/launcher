package com.example.persistence.entity;

import com.example.util.AbstractJsonUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "BATCH_JOB_EXECUTION")
public class BatchJobExecution extends AbstractJsonUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_execution_id", nullable = false)
    private Long jobExecutionId;
    @Column(name = "status")
    private String status;
    @Column(name = "exit_code")
    private String exitCode;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToOne(mappedBy = "batchJobExecution", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BatchJobExecutionParams batchJobExecutionParams;

}
