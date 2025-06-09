package com.allstate.personal_data_service.batch.config;

import com.allstate.personal_data_service.model.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@Profile("batch")
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ItemReader<UserProfile> reader;
    private final ItemProcessor<UserProfile, UserProfile> processor;
    private final ItemWriter<UserProfile> writer;

    @Bean
    public Step userProfileMigrationStep(){
        return new StepBuilder("userProfileMigrationStep", jobRepository)
                .<UserProfile, UserProfile>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job userProfileMigrationJob(){
        return new JobBuilder("userProfileMigrationJob", jobRepository)
                .start(extractFromOracleStep())
                .build();
    }

}