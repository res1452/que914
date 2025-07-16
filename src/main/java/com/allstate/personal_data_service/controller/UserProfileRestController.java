package com.allstate.personal_data_service.controller;

import com.allstate.personal_data_service.dto.UserProfileDTO;
import com.allstate.personal_data_service.dto.UserProfileResponse;
import com.allstate.personal_data_service.model.UserProfile;
import com.allstate.personal_data_service.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileRestController {

    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@Valid @PathVariable Long id) {
        UserProfile user = userProfileService.getUserById(id);
        return  ResponseEntity.ok(userProfileService.mapToResponse(user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserProfileResponse> getUserByEmail(@Valid @PathVariable String email){
        Optional<UserProfile> userOpt = userProfileService.getUserByEmail(email);
        return userOpt.map(user -> ResponseEntity.ok(userProfileService.mapToResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> createUserProfile(@Valid @RequestBody UserProfileDTO userProfile) {
        UserProfile savedUser = userProfileService.createUser(userProfile);
        return ResponseEntity.ok(userProfileService.mapToResponse(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> updateUserProfile(@PathVariable Long id, @Valid @RequestBody UserProfileDTO userProfile) {
        UserProfile updatedUser = userProfileService.updateUser(id, userProfile);
        return ResponseEntity.ok(userProfileService.mapToResponse(updatedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserProfileResponse> patchUserProfile(@PathVariable Long id, @Valid @RequestBody Map<String, Object> userProfile) {
        UserProfile patchedUser = userProfileService.patchUser(id, userProfile);
        return ResponseEntity.ok(userProfileService.mapToResponse(patchedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserById(id);
        return  ResponseEntity.noContent().build();
    }
}
