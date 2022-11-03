package com.example.service.batch.impl;

import com.example.persistence.BatchStatus;
import com.example.persistence.entity.BatchJobExecution;
import com.example.persistence.repo.BatchJobExecutionRepo;
import com.example.service.batch.BatchJobExeService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.Objects;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;

@ApplicationScoped
//@RequiredArgsConstructor
public class BatchJobExeServiceImpl implements BatchJobExeService {

    private final BatchJobExecutionRepo batchJobExecutionRepo;

    @Inject
    public BatchJobExeServiceImpl(BatchJobExecutionRepo batchJobExecutionRepo) {
        this.batchJobExecutionRepo = batchJobExecutionRepo;
    }

    @Override
    public boolean batchAlreadyExist(final String fileName) {
        //Scan for any SQL-Inject characters.
       final BatchJobExecution bj = batchJobExecutionRepo.findBatchJobExecutionByBatchJobExecutionParams_StringValContains(fileName);

        if (nonNull(bj) && Objects.equals(bj.getStatus(), BatchStatus.COMPLETED.name())) {
            //move file to an ARCHIVE directory. Hint: Use the SftpOpService
        } else if (nonNull(bj) && Objects.equals(bj.getStatus(), BatchStatus.FAILED.name())) {
            //move file to an ERROR directory. Hint: Use the SftpOpService
        }

        return false;
    }
}
