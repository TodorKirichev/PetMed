package com.petMed.email.client;

import com.petMed.email.dto.EmailNotificationData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-svc", url = "http://localhost:8081/api/notifications")
public interface EmailNotificationClient {

    @PostMapping("/send-email")
    void sendEmail(@RequestBody EmailNotificationData emailNotificationData);
}
