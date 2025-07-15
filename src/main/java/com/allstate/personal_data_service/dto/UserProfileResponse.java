package com.allstate.personal_data_service.dto;

import com.allstate.personal_data_service.model.Address;
import com.allstate.personal_data_service.model.Vehicle;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProfileResponse {

    private Long id;

    private String firstName;
    private String lastName;
    private String fullName;

    private String email;
    private String phoneNumber;

    private String dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String gender;
    private String maritalStatus;
    private String role;
    private String locale;
    private boolean wantsPromotions;

    private Address address;
    private Vehicle vehicle;
}
