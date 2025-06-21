package com.allstate.personal_data_service.controller;

import com.allstate.personal_data_service.dto.UserProfileDTO;
import com.allstate.personal_data_service.model.UserProfile;
import com.allstate.personal_data_service.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<UserProfile> createUser(@RequestBody UserProfileDTO dto){
        UserProfile savedProfile = userProfileService.createUser(dto);
        return ResponseEntity.ok(savedProfile);
    }
}
