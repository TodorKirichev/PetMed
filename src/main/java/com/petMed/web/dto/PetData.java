package com.petMed.web.dto;

import com.petMed.validation.ValidPhoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class PetData {

    @NotBlank(message = "Pet name cannot be empty")
    private String name;

    @NotBlank(message = "Please select a species")
    private String species;

    @NotBlank(message = "Please select a breed")
    private String breed;

    @NotNull(message = "Please select date of birth")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @ValidPhoto
    private MultipartFile photo;
}
