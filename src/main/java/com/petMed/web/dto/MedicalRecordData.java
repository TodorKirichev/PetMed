package com.petMed.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MedicalRecordData {

    @NotNull
    private UUID appointmentId;

    @NotBlank
    private String diagnosis;

    @NotBlank
    private String treatment;
}
