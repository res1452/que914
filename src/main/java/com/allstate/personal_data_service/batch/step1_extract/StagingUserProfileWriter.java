package com.allstate.personal_data_service.batch.step1_extract;

import com.allstate.personal_data_service.model.UserProfile;
import com.allstate.personal_data_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class StagingUserProfileWriter {

    private final UserProfileRepository userProfileRepository;

    @Bean
    public ItemWriter<UserProfile> writer() {
        return new ItemWriter<UserProfile>() {
            @Override
            public void write(Chunk<? extends UserProfile> items) {
                userProfileRepository.saveAll(items.getItems());
            }
        };
    }
}