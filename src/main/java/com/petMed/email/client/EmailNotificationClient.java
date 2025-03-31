package com.petMed.email.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "notification-svc", url = "http://localhost:8081/api/notifications")
public interface EmailNotificationClient {

}
