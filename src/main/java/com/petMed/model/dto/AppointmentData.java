package com.petMed.model.dto;

import com.petMed.model.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentData {

    private UUID appointmentId;

    private UUID petId;

    private String vetUsername;

    private LocalDate date;

    private LocalTime time;

}
