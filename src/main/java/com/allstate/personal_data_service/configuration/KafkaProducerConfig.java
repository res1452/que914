package com.allstate.personal_data_service.configuration;

import com.allstate.personal_data_service.avro.NotificationEmailAvro;
import com.allstate.personal_data_service.avro.SensitiveFieldUpdatedAvro;
import com.allstate.personal_data_service.avro.UserProfileUpdatedAvro;
import com.allstate.personal_data_service.event.NotificationEmailEvent;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.schema-registry-url}")
    private String schemaRegistryUrl;

    // ----- NotificationEmailEvent -----
    @Bean
    public ProducerFactory<String, UserProfileUpdatedAvro> userProfileUpdatedProducerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put("schema.registry.url", schemaRegistryUrl);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public KafkaTemplate<String, UserProfileUpdatedAvro> userProfileUpdatedKafkaTemplate(){
        return new KafkaTemplate<>(userProfileUpdatedProducerFactory());
    }

    // ----- NotificationEmailEvent -----
    @Bean
    public ProducerFactory<String, NotificationEmailAvro> notificationEmailProducerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put("schema.registry.url", schemaRegistryUrl);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, NotificationEmailAvro> notificationEmailKafkaTemplate(){
        return new KafkaTemplate<>(notificationEmailProducerFactory());
    }

    // ----- SensitiveFieldUpdatedEvent -----
    @Bean
    public ProducerFactory<String, SensitiveFieldUpdatedAvro> sensitiveFieldUpdatedAvroProducerFactory(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put("schema.registry.url", schemaRegistryUrl);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, SensitiveFieldUpdatedAvro> sensitiveFieldKafkaTemplate(){
        return new KafkaTemplate<>(sensitiveFieldUpdatedAvroProducerFactory());
    }
}