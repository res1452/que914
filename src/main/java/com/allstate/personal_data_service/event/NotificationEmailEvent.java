package com.allstate.personal_data_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEmailEvent {

    private String email;
    private String fullName;
    private String eventType;
    private String locale;
    private String timestamp;
}