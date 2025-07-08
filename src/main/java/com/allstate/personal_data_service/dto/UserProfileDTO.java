package com.allstate.personal_data_service.dto;

import com.allstate.personal_data_service.model.Address;
import com.allstate.personal_data_service.model.Vehicle;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDTO {
    @NotBlank(message = "First Name Required")
    private String firstName;
    @NotBlank(message = "Last Name Required")
    private String lastName;
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email Required")
    private String email;
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @NotNull(message = "DOB is required")
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
