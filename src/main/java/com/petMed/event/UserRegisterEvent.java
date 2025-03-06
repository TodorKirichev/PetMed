package com.petMed.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisterEvent extends ApplicationEvent {

    private final String firstName;

    private final String lastName;

    private final String email;

    public UserRegisterEvent(Object source, String firstName, String lastName, String email) {
        super(source);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
