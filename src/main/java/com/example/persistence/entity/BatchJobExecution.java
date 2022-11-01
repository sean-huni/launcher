package com.example.persistence.entity;

import com.example.util.AbstractJsonUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
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
