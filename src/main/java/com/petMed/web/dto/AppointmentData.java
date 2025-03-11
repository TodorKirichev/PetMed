package com.petMed.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentData {

    @NotNull
    private UUID petId;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

}
