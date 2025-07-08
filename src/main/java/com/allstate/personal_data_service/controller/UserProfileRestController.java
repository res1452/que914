package com.allstate.personal_data_service.controller;

import com.allstate.personal_data_service.dto.UserProfileDTO;
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
    public ResponseEntity<UserProfile> getUserProfile(@Valid @PathVariable Long id) {
        return  ResponseEntity.ok(userProfileService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserProfile> getUserByEmail(@Valid @PathVariable String email){
        return userProfileService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserProfile> createUserProfile(@Valid @RequestBody UserProfileDTO userProfile) {
        return ResponseEntity.ok(userProfileService.createUser(userProfile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long id, @Valid @RequestBody UserProfileDTO userProfile) {
        return ResponseEntity.ok(userProfileService.updateUser(id, userProfile));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserProfile> patchUserProfile(@PathVariable Long id, @Valid @RequestBody Map<String, Object> userProfile) {
        return ResponseEntity.ok(userProfileService.patchUser(id, userProfile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserById(id);
        return  ResponseEntity.noContent().build();
    }
}
