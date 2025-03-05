package com.petMed.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationData {

    private String email;

    private String subject;

    private String body;
}
