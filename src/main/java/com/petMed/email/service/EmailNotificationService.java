package com.petMed.email.service;

import com.petMed.email.client.EmailNotificationClient;
import com.petMed.event.AppointmentBookedEvent;
import com.petMed.event.UserRegisterEvent;
import com.petMed.email.dto.EmailNotificationData;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.petMed.email.EmailTemplates.*;

@Service
public class EmailNotificationService {

    private final EmailNotificationClient emailNotificationClient;

    public EmailNotificationService(EmailNotificationClient emailNotificationClient) {
        this.emailNotificationClient = emailNotificationClient;
    }

    @Async
    @EventListener
    public void sendConfirmRegistrationEmail(UserRegisterEvent userRegisterEvent) {
        EmailNotificationData emailNotificationData = createNotification(userRegisterEvent);
        emailNotificationClient.sendEmail(emailNotificationData);
    }

    private EmailNotificationData createNotification(UserRegisterEvent event) {
        return EmailNotificationData.builder()
                .email(event.getEmail())
                .subject(CONFIRM_REGISTRATION_SUBJECT)
                .body(String.format(CONFIRM_REGISTRATION_BODY, event.getFirstName(), event.getLastName()))
                .build();
    }

    @Async
    @EventListener
    public void sendAppointmentBookedEmail(AppointmentBookedEvent appointmentBookedEvent) {
        EmailNotificationData emailNotificationData = createNotification(appointmentBookedEvent);
        emailNotificationClient.sendEmail(emailNotificationData);
    }

    private EmailNotificationData createNotification(AppointmentBookedEvent event) {
        return EmailNotificationData.builder()
                .email(event.getPetOwnerEmail())
                .subject(APPOINTMENT_BOOKED_SUBJECT)
                .body(String.format(APPOINTMENT_BOOKED_BODY,
                        event.getVetFirstName(),
                        event.getVetLastName(),
                        event.getPetSpecies(),
                        event.getPetName(),
                        event.getDate(),
                        event.getTime()))
                .build();
    }
}
