package com.example.service.batch.impl;

import com.example.persistence.BatchStatus;
import com.example.persistence.entity.BatchJobExecution;
import com.example.persistence.repo.BatchJobExecutionRepo;
import com.example.service.batch.BatchJobExeService;
import com.example.service.file.FileOpService;
import com.example.service.file.Instruction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;

import java.util.Objects;

import static java.util.Objects.nonNull;

@ApplicationScoped
public class BatchJobExeServiceImpl implements BatchJobExeService {
    private final FileOpService fileOpService;
    private final BatchJobExecutionRepo batchJobExecutionRepo;

    @Inject
    public BatchJobExeServiceImpl(FileOpService fileOpService, BatchJobExecutionRepo batchJobExecutionRepo) {
        this.fileOpService = fileOpService;
        this.batchJobExecutionRepo = batchJobExecutionRepo;
    }

    @Override
    @SneakyThrows
    public boolean batchAlreadyExist(final String fileName) {
        //Scan for any SQL-Inject characters.
        final BatchJobExecution bj = batchJobExecutionRepo.findBatchJobExecutionByBatchJobExecutionParams_StringValContains(fileName);

        if (nonNull(bj) && Objects.equals(bj.getStatus(), BatchStatus.COMPLETED.name())) {
            //ToDo: move file to an ARCHIVE directory. Hint: Use the FileOpService
            fileOpService.moveFile("", Instruction.INPUT_TO_ARCHIVE, bj.getBatchJobExecutionParams().getStringVal());
        } else if (nonNull(bj) && Objects.equals(bj.getStatus(), BatchStatus.FAILED.name())) {
            //ToDo: move file to an ERROR directory. Hint: Use the FileOpService
            fileOpService.moveFile("", Instruction.ARCHIVE_TO_ERROR, bj.getBatchJobExecutionParams().getStringVal());
        }

        return false;
    }
}
