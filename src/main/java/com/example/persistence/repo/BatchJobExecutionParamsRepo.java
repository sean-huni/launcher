package com.example.persistence.repo;

import com.example.persistence.entity.BatchJobExecutionParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobExecutionParamsRepo extends JpaRepository<BatchJobExecutionParams, Long> {

}
