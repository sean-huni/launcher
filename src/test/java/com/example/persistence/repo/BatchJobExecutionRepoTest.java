package com.example.persistence.repo;

import com.example.persistence.entity.BatchJobExecution;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void findBatchJobExecutionByBatchJobExecutionParams_StringValContains() {
        final BatchJobExecution resp = batchJobExecutionRepo.findBatchJobExecutionByBatchJobExecutionParams_StringValContains("100012037_2022-09-17_18.45.48.525454_4174-89FB");

        log.info("JobExecutionId: {}", resp.getJobExecutionId());
        log.info("CreateTime: {}", resp.getCreateTime());
        log.info("StartTime: {}", resp.getStartTime());
        log.info("EndTime: {}", resp.getEndTime());
        log.info("AbsolutePath: {}", resp.getBatchJobExecutionParams().getStringVal());
        assertNotNull(resp);
        assertEquals("file:///Users/sean/env/pty/proj/microservices/pay-cons/src/main/resources/samples" +
                "/100012037_2022-09-17_18.45.48.525454_4174-89FB.csv", resp.getBatchJobExecutionParams().getStringVal());
    }
}