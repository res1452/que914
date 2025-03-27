package com.allstate.personal_data_service.service;

import com.allstate.personal_data_service.model.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalInformationProducer {

    private final KafkaTemplate<String, UserProfile> kafkaTemplate;
}
