package com.allstate.personal_data_service.service.serviceImpl;

import com.allstate.personal_data_service.dto.UserProfileDTO;
import com.allstate.personal_data_service.event.NotificationEmailEvent;
import com.allstate.personal_data_service.event.UserProfileUpdatedEvent;
import com.allstate.personal_data_service.model.UserProfile;
import com.allstate.personal_data_service.repository.UserProfileRepository;
import com.allstate.personal_data_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final RedisTemplate<String, UserProfile> redisTemplate;

    private final NotificationEmailEventProducer notificationEmailEventProducer;


    private static final String CACHE_PREFIX = "user";

    private final UserProfileEventProducer userProfileEventProducer;

    @Override
    @Cacheable(value = "userProfiles", key = "#id")
    public UserProfile getUserById(Long id) {
        String cacheKey = CACHE_PREFIX + id;

        //Try fetching from the Redis server first
        UserProfile cachedUser = (UserProfile) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        //If not in the cache, get from the database
        UserProfile user = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Store in the Redis cache for 10 minutes
        redisTemplate.opsForValue().set(cacheKey,user, Duration.ofMinutes(10));

        return user;
    }

    @Override
    public UserProfile createUser(UserProfileDTO dto){
        UserProfile userProfile = new  UserProfile();

        //Manual DTO -> Entity mapping
        userProfile.setFirstName(dto.getFirstName());
        userProfile.setLastName(dto.getLastName());
        userProfile.setEmail(dto.getEmail());
        userProfile.setPhoneNumber(dto.getPhoneNumber());
        userProfile.setDateOfBirth(dto.getDateOfBirth());
        userProfile.setGender(dto.getGender());
        userProfile.setMaritalStatus(dto.getMaritalStatus());
        userProfile.setAddress(dto.getAddress());
        userProfile.setVehicle(dto.getVehicle());
        userProfile.setRole(dto.getRole());
        userProfile.setCacheable(dto.isCacheable());
        userProfile.setWantsPromotions(dto.isWantsPromotions());
        userProfile.setLocale(dto.getLocale());

        //Save to database
        UserProfile savedUser = userProfileRepository.save(userProfile);

        // Store in Redis with 24-hour expiration
        String cacheKey = CACHE_PREFIX + userProfile.getId();
        redisTemplate.opsForValue().set(cacheKey, savedUser, Duration.ofMinutes(30));

        UserProfileUpdatedEvent event = new UserProfileUpdatedEvent(savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getPhoneNumber(),
                "system",
                LocalDateTime.now()
        );
        userProfileEventProducer.sendUserProfileEvent(event);

        //Send promotional opt-in event if applicable
        if(dto.isWantsPromotions()){
            NotificationEmailEvent promoEvent = new NotificationEmailEvent(
                    savedUser.getEmail(),
                    savedUser.getFullName(),
                    "MARKETING_OPTIN",
                    savedUser.getLocale(),
                    Instant.now().toString()
            );
            notificationEmailEventProducer.send(promoEvent);
        }

        return savedUser;
    }

    @Override
    @CacheEvict(value = "userProfiles", key = "#id")
    public UserProfile updateUser(Long id, UserProfile userProfile){
        UserProfile existingUser = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setFirstName(userProfile.getFirstName());
        existingUser.setLastName(userProfile.getLastName());
        existingUser.setEmail(userProfile.getEmail());
        existingUser.setPhoneNumber(userProfile.getPhoneNumber());
        existingUser.setAddress(userProfile.getAddress());
        existingUser.setGender(userProfile.getGender());
        existingUser.setMaritalStatus(userProfile.getMaritalStatus());

        UserProfile savedUser = userProfileRepository.save(existingUser);
        String cacheKey = CACHE_PREFIX + id;
        redisTemplate.opsForValue().set(cacheKey, savedUser, Duration.ofMinutes(30));

        UserProfileUpdatedEvent event = new UserProfileUpdatedEvent(savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getPhoneNumber(),
                "system",
                LocalDateTime.now()
        );

        userProfileEventProducer.sendUserProfileEvent(event);

        return savedUser;
    }

    @Override
    @CacheEvict(value = "userProfiles", key = "#id")
    public UserProfile patchUser(Long id, Map<String, Object> updates){
        UserProfile existingUser = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Set<String> allowedFields = Set.of(
                "firstName", "lastName", "email", "phoneNumber",
                "address", "gender", "maritalStatus");

        updates.forEach((key, value) -> {
            if(allowedFields.contains(key)) {
                Field field = ReflectionUtils.findField(UserProfile.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    try {
                        ReflectionUtils.setField(field, existingUser, value);
                        log.info("Patched field: {} = {}", key, value);
                    } catch (IllegalArgumentException e) {
                        log.warn("Failed to set field {} with value {}", key, value);
                    }
                }
            }
        });

        UserProfile savedUser = userProfileRepository.save(existingUser);
        String cacheKey = CACHE_PREFIX + id;
        redisTemplate.opsForValue().set(cacheKey, savedUser, Duration.ofMinutes(30));
        return savedUser;
    }

    @Override
    @CacheEvict(value = "userProfiles", key = "#id")
    public void deleteUserById(Long id){
        if(!userProfileRepository.existsById(id)){
            throw new RuntimeException("User not found with ID: " + id);
        }

        userProfileRepository.deleteById(id);

        //Remove from Redis Cache
        String cacheKey = CACHE_PREFIX + id;
        redisTemplate.delete(cacheKey);
    }

    @Override
    @Cacheable(value = "userProfiles", key = "#email")
    public Optional<UserProfile> getUserByEmail(String email){
        //Try fetching from Redis first
        String cacheKey = CACHE_PREFIX + email;

        UserProfile cachedUser = redisTemplate.opsForValue().get(cacheKey);
        if(cachedUser != null){
            return Optional.of(cachedUser);
        }

        //If not in cache, fetch from DB
        Optional<UserProfile> user = userProfileRepository.findByEmail(email);
        //Place the email in the cache
        user.ifPresent(userProfile -> redisTemplate.opsForValue().set(cacheKey, userProfile, Duration.ofHours(24)));

        return user;
    }

}
