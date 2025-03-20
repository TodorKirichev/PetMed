package com.petMed.event;

import com.petMed.user.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisterEvent extends ApplicationEvent {

    private final String firstName;
    private final String lastName;
    private final String email;

    public UserRegisterEvent(User user) {
        super(user);
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
