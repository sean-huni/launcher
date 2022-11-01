package com.example.service.batch;

import com.example.persistence.BatchStatus;
import com.example.persistence.entity.BatchJobExecution;
import com.example.persistence.repo.BatchJobExecutionRepo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.Objects;

import static java.util.Objects.nonNull;

@ApplicationScoped
//@RequiredArgsConstructor
public class BatchJobExeServiceImpl implements BatchJobExeService {

    private final BatchJobExecutionRepo batchJobExecutionRepo;

    @Inject
    public BatchJobExeServiceImpl(BatchJobExecutionRepo batchJobExecutionRepo) {
        this.batchJobExecutionRepo = batchJobExecutionRepo;
    }

    @Override
    public boolean bachAlreadyExist(final String fileName) {
       final BatchJobExecution bj = batchJobExecutionRepo.findBatchJobExecutionByBatchJobExecutionParams_StringValContains(fileName);

        if (nonNull(bj) && Objects.equals(bj.getStatus(), BatchStatus.COMPLETED.name())) {

        }


        return false;
    }
}
