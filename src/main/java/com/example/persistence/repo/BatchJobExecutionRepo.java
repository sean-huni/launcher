package com.example.persistence.repo;

import com.example.persistence.entity.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobExecutionRepo extends JpaRepository<BatchJobExecution, Long> {

    BatchJobExecution findBatchJobExecutionByBatchJobExecutionParams_StringValContains(final String fileName);

    boolean existsBatchJobExecutionsByBatchJobExecutionParams_StringValContains(final String fileName);
}
