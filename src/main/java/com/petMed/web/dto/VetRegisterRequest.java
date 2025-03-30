package com.petMed.web.dto;

import com.petMed.clinic.model.CityName;
import com.petMed.validation.ValidPhoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VetRegisterRequest extends RegisterRequest{

    @ValidPhoto
    private MultipartFile photo;

    @NotBlank(message = "Clinic name cannot be empty")
    @Size(max = 100, message = "Clinic name cannot exceed 100 characters")
    private String clinicName;

    @NotNull(message = "Please select a city")
    private CityName city;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 100, message = "Address cannot exceed 100 characters")
    private String address;

    @URL(message = "Invalid URL format")
    @Size(max = 100, message = "Site name cannot exceed 100 characters")
    private String site;

}
