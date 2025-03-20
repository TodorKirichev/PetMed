package com.petMed.event;

import com.petMed.appointment.model.Appointment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class AppointmentBookedEvent extends ApplicationEvent {

    private final String petOwnerEmail;
    private final String vetFirstName;
    private final String vetLastName;
    private final String petName;
    private final String petSpecies;
    private final LocalDate date;
    private final LocalTime time;

    public AppointmentBookedEvent(Appointment appointment) {
        super(appointment);
        this.petOwnerEmail = appointment.getPet().getOwner().getEmail();
        this.vetFirstName = appointment.getVet().getFirstName();
        this.vetLastName = appointment.getVet().getLastName();
        this.petName = appointment.getPet().getName();
        this.petSpecies = appointment.getPet().getSpecies().getSpeciesName();
        this.date = appointment.getDate();
        this.time = appointment.getStartTime();
    }
}
