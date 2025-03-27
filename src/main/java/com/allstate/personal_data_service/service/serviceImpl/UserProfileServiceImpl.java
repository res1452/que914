package com.allstate.personal_data_service.service.serviceImpl;

import com.allstate.personal_data_service.model.UserProfile;
import com.allstate.personal_data_service.repository.UserProfileRepository;
import com.allstate.personal_data_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final RedisTemplate<String, UserProfile> redisTemplate;

    private static final String CACHE_PREFIX = "user";

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
    @CachePut(value = "userProfiles", key = "#userProfile.id")
    public UserProfile createUser(UserProfile userProfile){
        UserProfile savedUser = userProfileRepository.save(userProfile);

        // Store in Redis with 24-hour expiration
        String cacheKey = CACHE_PREFIX + userProfile.getId();
        redisTemplate.opsForValue().set(cacheKey, savedUser, Duration.ofHours(24));

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
