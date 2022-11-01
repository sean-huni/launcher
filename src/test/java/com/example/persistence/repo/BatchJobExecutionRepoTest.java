package com.example.persistence.repo;

import com.example.persistence.entity.BatchJobExecution;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
@QuarkusTest
class BatchJobExecutionRepoTest {
    @Inject
    BatchJobExecutionRepo batchJobExecutionRepo;

    @BeforeEach
    void setUp() {
        assertNotNull(batchJobExecutionRepo);
        log.info("batchJobExecutionRepo is not null");
    }

    @Test
    @DisplayName("Find Batch File - Positive Test")
    void findBatchJobExecutionByBatchJobExecutionParams_StringValContains() {
        final BatchJobExecution resp = batchJobExecutionRepo.findBatchJobExecutionByBatchJobExecutionParams_StringValContains("100012037_2022-09-17_18.45.48.525454_4174-89FB");

        log.info("JobExecutionId: {}", resp.getJobExecutionId());
        log.info("Status: {}", resp.getStatus());
        log.info("ExitCode: {}", resp.getExitCode());
        log.info("CreateTime: {}", resp.getCreateTime());
        log.info("StartTime: {}", resp.getStartTime());
        log.info("EndTime: {}", resp.getEndTime());
        log.info("AbsolutePath: {}", resp.getBatchJobExecutionParams().getStringVal());
        assertNotNull(resp);
        assertEquals("file:///Users/sean/env/pty/proj/microservices/pay-cons/src/main/resources/samples" +
                "/100012037_2022-09-17_18.45.48.525454_4174-89FB.csv", resp.getBatchJobExecutionParams().getStringVal());
    }

    @Test
    @DisplayName("Find Non-Existent Batch File - Negative Test")
    void findNonExistentBatchJobExecutionByBatchJobExecutionParams_StringValContains() {
        final BatchJobExecution resp = batchJobExecutionRepo.findBatchJobExecutionByBatchJobExecutionParams_StringValContains("NON-EXISTENT");

        assertNull(resp); //Supposed to be null
    }

    @Test
    @DisplayName("Find Existent Batch File - Positive Test")
    void existByBatchJobExecutionByBatchJobExecutionParams_StringValContains_returnTrue() {
        final boolean resp = batchJobExecutionRepo.existsBatchJobExecutionsByBatchJobExecutionParams_StringValContains("100012037_2022-09-17_18.45.48.525454_4174-89FB");
        assertTrue(resp);
    }

    @Test
    @DisplayName("Find Non-Existent Batch File - Negative Test")
    void existByBatchJobExecutionByBatchJobExecutionParams_StringValContains_returnFalse() {
        final boolean resp = batchJobExecutionRepo.existsBatchJobExecutionsByBatchJobExecutionParams_StringValContains("NON-EXISTENT");
        assertFalse(resp);
    }
}