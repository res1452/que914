package com.allstate.personal_data_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class MigrationController {

    private final JobLauncher jobLauncher;
    private final Job userProfileMigrationJob;
    private static final Logger logger = LoggerFactory.getLogger(MigrationController.class);

    @PostMapping("/run-migration")
    public ResponseEntity<String> runMigrationJob() {
        try {
            jobLauncher.run(userProfileMigrationJob, new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters());

            return ResponseEntity.ok("Migration Job triggered successfully!");
        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            logger.error("Failed to run migration job", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to trigger migration job: " + e.getMessage());
        }
    }
}