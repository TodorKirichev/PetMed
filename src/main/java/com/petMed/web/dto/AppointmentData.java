package com.petMed.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Please enter a reason")
    @Size(max = 200, message = "Reason cannot exceed 200 characters")
    private String reason;

}
