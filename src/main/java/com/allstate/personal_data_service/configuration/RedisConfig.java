package com.allstate.personal_data_service.configuration;

import com.allstate.personal_data_service.model.UserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, UserProfile> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, UserProfile> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        //Serialize keys as Strings
        template.setKeySerializer(new StringRedisSerializer());

        //Serialize values as JSON (for storing complex objects like UserProfiles)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
