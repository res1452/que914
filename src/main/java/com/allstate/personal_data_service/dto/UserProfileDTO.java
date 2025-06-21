package com.allstate.personal_data_service.dto;

import com.allstate.personal_data_service.model.Address;
import com.allstate.personal_data_service.model.Vehicle;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String maritalStatus;

    private Address address;
    private Vehicle vehicle;

    private String role;
    private boolean cacheable;

    private boolean wantsPromotions;
    private String locale;
}
