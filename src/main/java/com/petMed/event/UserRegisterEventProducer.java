package com.petMed.event;

import com.petMed.event.payload.UserRegisterEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterEventProducer {

    private final KafkaTemplate<String, UserRegisterEvent> kafkaTemplate;

    public UserRegisterEventProducer(KafkaTemplate<String, UserRegisterEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UserRegisterEvent event) {
        kafkaTemplate.send("user-register-event", event);
    }
}
