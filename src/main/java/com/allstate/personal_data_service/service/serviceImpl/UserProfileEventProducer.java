package com.allstate.personal_data_service.service.serviceImpl;

import com.allstate.personal_data_service.event.UserProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileEventProducer {
     private final KafkaTemplate<String, Object> kafkaTemplate;

     private static final String TOPIC = "user_profile_events";

     public void sendUserProfileEvent(UserProfileUpdatedEvent event){
         kafkaTemplate.send(TOPIC, event.getUserId().toString(), event);
         log.info("Sent UserProfileUpdatedEvent to Kafka for userId={} -> {}", event.getUserId(), event);
     }
}
