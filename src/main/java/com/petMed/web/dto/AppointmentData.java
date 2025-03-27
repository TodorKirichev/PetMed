package com.petMed.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentData {

    @NotNull(message = "Please select a pet")
    private UUID petId;

    @NotNull(message = "Please select a date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @NotNull(message = "Time was not selected")
    private LocalTime time;

}
