package com.petMed.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AppointmentInfo {

    private UUID appointmentId;

    private String date;

    private String startTime;

    private String reason;

    private String petName;

    private String petSpecies;

    private String petBreed;

    private String petAge;

    private String petOwnerName;

    private String petOwnerEmail;

    private String petOwnerPhone;

    private String petImageUrl;

    private boolean hasMedicalRecord;
}
