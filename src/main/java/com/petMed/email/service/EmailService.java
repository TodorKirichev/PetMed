package com.petMed.email.service;

import com.petMed.email.client.NotificationClient;
import com.petMed.event.UserRegisterEvent;
import com.petMed.web.dto.NotificationData;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.petMed.email.EmailTemplates.CONFIRM_EMAIL_BODY;
import static com.petMed.email.EmailTemplates.CONFIRM_EMAIL_SUBJECT;

@Service
public class EmailService {

    private final NotificationClient notificationClient;

    public EmailService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @Async
    @EventListener
    public void handleUserRegisteredEvent(UserRegisterEvent event) {

        NotificationData notificationData = NotificationData.builder()
                .email(event.getEmail())
                .subject(CONFIRM_EMAIL_SUBJECT)
                .body(String.format(CONFIRM_EMAIL_BODY, event.getFirstName(), event.getLastName()))
                .build();

        notificationClient.sendEmail(notificationData);
    }
}
