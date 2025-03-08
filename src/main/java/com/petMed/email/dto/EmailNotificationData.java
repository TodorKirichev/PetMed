package com.petMed.email.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailNotificationData {

    private String email;

    private String subject;

    private String body;
}
