package com.allstate.personal_data_service.service;

import com.allstate.personal_data_service.model.UserProfile;

import java.util.Optional;

public interface UserProfileService {

    UserProfile getUserById(Long id);

    UserProfile createUser(UserProfile userProfile);

    void deleteUserById(Long id);

    Optional<UserProfile> getUserByEmail(String email);
}