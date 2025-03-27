package com.allstate.personal_data_service.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RiskAssessmentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public RiskAssessmentProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message){

    }
}
