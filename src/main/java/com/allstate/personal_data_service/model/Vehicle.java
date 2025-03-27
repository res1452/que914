package com.allstate.personal_data_service.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    private String make;
    private String model;
    private String color;
    private Integer year;
    private Integer VIN;
    private LocalDate registrationDate;
    private LocalDate purchaseDate;
}
