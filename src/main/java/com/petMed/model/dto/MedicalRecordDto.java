package com.petMed.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class MedicalRecordDto {

    private String appointmentId;

    private String diagnosis;

    private String treatment;
}
