package com.petMed.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentBookedData {

    private String petOwnerEmail;
    private String petOwnerUsername;
    private String vetFirstName;
    private String vetLastName;
    private String petName;
    private String petSpecies;
    private LocalDate date;
    private LocalTime time;

}
