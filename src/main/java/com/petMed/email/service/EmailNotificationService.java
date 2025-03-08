package com.petMed.email.service;

import com.petMed.email.client.EmailNotificationClient;
import com.petMed.event.UserRegisterEvent;
import com.petMed.email.dto.EmailNotificationData;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.petMed.email.EmailTemplates.CONFIRM_EMAIL_BODY;
import static com.petMed.email.EmailTemplates.CONFIRM_EMAIL_SUBJECT;

@Service
public class EmailNotificationService {

    private final EmailNotificationClient emailNotificationClient;

    public EmailNotificationService(EmailNotificationClient emailNotificationClient) {
        this.emailNotificationClient = emailNotificationClient;
    }

    @Async
    @EventListener
    public void sendRegistrationEmail(UserRegisterEvent userRegisterEvent) {
        EmailNotificationData emailNotificationData = createNotification(userRegisterEvent);
        emailNotificationClient.sendEmail(emailNotificationData);
    }

    private EmailNotificationData createNotification(UserRegisterEvent event) {
        return EmailNotificationData.builder()
                .email(event.getEmail())
                .subject(CONFIRM_EMAIL_SUBJECT)
                .body(String.format(CONFIRM_EMAIL_BODY, event.getFirstName(), event.getLastName()))
                .build();
    }
}
