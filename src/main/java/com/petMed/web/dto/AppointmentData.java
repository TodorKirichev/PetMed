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

    @NotNull(message = "Please select a pet")
    private UUID petId;

    @NotNull(message = "Please select a date")
    private LocalDate date;

    @NotNull(message = "Please select a time")
    private LocalTime time;

}
