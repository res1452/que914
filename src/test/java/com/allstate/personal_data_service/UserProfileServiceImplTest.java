package com.allstate.personal_data_service;

import com.allstate.personal_data_service.model.Address;
import com.allstate.personal_data_service.model.UserProfile;
import com.allstate.personal_data_service.model.Vehicle;
import com.allstate.personal_data_service.repository.UserProfileRepository;
import com.allstate.personal_data_service.service.UserProfileService;
import com.allstate.personal_data_service.service.serviceImpl.UserProfileEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private RedisTemplate<String, UserProfile> redisTemplate;

    @Mock
    private ValueOperations<String, UserProfile> valueOperations;

    @Mock
    private UserProfileEventProducer userProfileEventProducer;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserProfile user;

    @BeforeEach
    void setUp() {
        Address address = new Address("123 Main St", "Minneapolis", "MN", "55401");
        Vehicle vehicle = new Vehicle("Ford", "Mustang", "Blue", 2020, 123456, LocalDate.now(), LocalDate.now());

        user = new UserProfile();
        user.setId(1L);
        user.setFirstName("Daniel");
        user.setLastName("Alemayehu");
        user.setEmail("daniel@gmail.com");
        user.setPhoneNumber("555-1234");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setGender("Male");
        user.setMaritalStatus("Single");
        user.setAddress(address);
        user.setVehicle(vehicle);
        user.setRole("user");
        user.setCacheable(true);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetUserById_userFoundInCache(){
        when(valueOperations.get("user1")).thenReturn(user);

        UserProfile result = userProfileService.getUserById(1L);

        assertEquals("Daniel", result.getFirstName());
        verify(valueOperations).get("user1");
        verify(userProfileRepository, never()).findById(any());
    }

}
