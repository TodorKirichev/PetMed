package com.petMed.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AppointmentDTO {

    private UUID appointmentId;

    private String startTime;

    private String petName;

    private String petSpecies;

    private String petBreed;

    private String petOwnerFirstName;
}
