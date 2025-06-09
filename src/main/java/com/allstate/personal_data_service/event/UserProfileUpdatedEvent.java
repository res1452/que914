package com.allstate.personal_data_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdatedEvent {
    private Long userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String updatedBy;
    private LocalDateTime updatedOn;
}
