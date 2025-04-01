package com.petMed.notification.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.notification.client.NotificationClient;
import com.petMed.notification.dto.AppointmentBookedData;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @Async
    public void sendNotification(Appointment appointment) {
        AppointmentBookedData appointmentBookedData = createAppointmentNotification(appointment);
        notificationClient.sendAppointmentBookedNotification(appointmentBookedData);
    }

    private AppointmentBookedData createAppointmentNotification(Appointment appointment) {
        AppointmentBookedData event = new AppointmentBookedData();
        event.setPetOwnerUsername(appointment.getPet().getOwner().getUsername());
        event.setPetOwnerEmail(appointment.getPet().getOwner().getEmail());
        event.setVetFirstName(appointment.getVet().getFirstName());
        event.setVetLastName(appointment.getVet().getLastName());
        event.setPetName(appointment.getPet().getName());
        event.setPetSpecies(appointment.getPet().getSpecies().getSpeciesName());
        event.setDate(appointment.getDate());
        event.setTime(appointment.getStartTime());

        return event;
    }
}
