package com.allstate.personal_data_service.service;

import com.allstate.personal_data_service.dto.UserProfileDTO;
import com.allstate.personal_data_service.dto.UserProfileResponse;
import com.allstate.personal_data_service.model.UserProfile;

import java.util.Map;
import java.util.Optional;

public interface UserProfileService {

    UserProfile getUserById(Long id);

    UserProfile createUser(UserProfileDTO dto);

    UserProfile updateUser(Long id, UserProfileDTO userProfile);

    UserProfile patchUser(Long id, Map<String, Object> updates);

    void deleteUserById(Long id);

    Optional<UserProfile> getUserByEmail(String email);

    UserProfileResponse mapToResponse(UserProfile user);

}