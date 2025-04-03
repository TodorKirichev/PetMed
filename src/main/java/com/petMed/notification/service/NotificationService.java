package com.petMed.notification.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.mapper.AppointmentMapper;
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
        AppointmentBookedData appointmentBookedData = AppointmentMapper.mapToAppointmentBookedData(appointment);
        notificationClient.sendAppointmentBookedNotification(appointmentBookedData);
    }
}
