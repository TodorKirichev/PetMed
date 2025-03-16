package com.petMed.web.dto;

import com.petMed.validation.ValidPhoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PetData {

    @NotBlank(message = "Pet name cannot be empty")
    private String name;

    @NotBlank(message = "Please select a species")
    private String species;

    @NotBlank(message = "Please select a breed")
    private String breed;

    @Positive(message = "Age must be positive")
    @NotNull(message = "Age cannot be empty")
    private Integer age;

    @ValidPhoto
    private MultipartFile photo;
}
