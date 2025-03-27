package com.allstate.personal_data_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //Basic personal information
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String maritalStatus;

    //Address Information
    @Embedded
    private Address address;

    //Vehicle Information
    @Embedded
    private Vehicle vehicle;

    //Security and Authentication
    private String role;

    //Caching & API Optimization
    private boolean cacheable;
}
