package com.allstate.personal_data_service.repository;

import com.allstate.personal_data_service.model.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    Optional<UserProfile> findByEmail(String email);
}
