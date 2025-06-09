package com.allstate.personal_data_service.service;

import com.allstate.personal_data_service.model.UserProfile;

import java.util.Map;
import java.util.Optional;

public interface UserProfileService {

    UserProfile getUserById(Long id);

    UserProfile createUser(UserProfile userProfile);

    UserProfile updateUser(Long id, UserProfile userProfile);

    UserProfile patchUser(Long id, Map<String, Object> updates);

    void deleteUserById(Long id);

    Optional<UserProfile> getUserByEmail(String email);
}