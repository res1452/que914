package com.allstate.personal_data_service.service.serviceImpl;

import com.allstate.personal_data_service.event.NotificationEmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationEmailEventProducer {

    private final String TOPIC = "notification.email.outbound";

    private final KafkaTemplate<String, NotificationEmailEvent> kafkaTemplate;

    @Autowired
    public NotificationEmailEventProducer(KafkaTemplate<String, NotificationEmailEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(NotificationEmailEvent event){
        log.info("Producing opt-in notification event: {}", event);
        kafkaTemplate.send(TOPIC, event);
    }

}
