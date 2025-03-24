package com.petMed.email.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.email.client.EmailNotificationClient;
import com.petMed.email.dto.EmailNotificationData;
import com.petMed.event.AppointmentBookedEvent;
import com.petMed.event.UserRegisterEvent;
import com.petMed.pet.model.Pet;
import com.petMed.pet.model.PetSpecies;
import com.petMed.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailNotificationServiceUTest {

    @Mock
    private EmailNotificationClient emailNotificationClient;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Test
    void sendAppointmentBookedEmail_Success() {
        User owner = User.builder()
                .email("owner@petMed.com")
                .build();

        Pet pet = Pet.builder()
                .name("Timo")
                .species(PetSpecies.CAT)
                .owner(owner)
                .build();

        User vet = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();

        Appointment appointment = Appointment.builder()
                .pet(pet)
                .vet(vet)
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .build();

        AppointmentBookedEvent appointmentBookedEvent = new AppointmentBookedEvent(appointment);

        emailNotificationService.sendAppointmentBookedEmail(appointmentBookedEvent);

        verify(emailNotificationClient, times(1)).sendEmail(any());
    }

    @Test
    void sendConfirmRegistrationEmail_Success() {
        User user = new User();
        user.setEmail("user@petMed.com");

        UserRegisterEvent userRegisterEvent = new UserRegisterEvent(user);

        emailNotificationService.sendConfirmRegistrationEmail(userRegisterEvent);

        verify(emailNotificationClient, times(1)).sendEmail(any());
    }
}
