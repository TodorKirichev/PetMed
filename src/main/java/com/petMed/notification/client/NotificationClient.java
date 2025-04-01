package com.petMed.notification.client;

import com.petMed.notification.dto.AppointmentBookedData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-svc", url = "http://localhost:8081/api/notifications")
public interface NotificationClient {

    @PostMapping("/appointments")
    void sendAppointmentBookedNotification(@RequestBody AppointmentBookedData data);
}
