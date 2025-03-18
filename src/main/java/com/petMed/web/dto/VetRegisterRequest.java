package com.petMed.web.dto;

import com.petMed.clinic.model.CityName;
import com.petMed.validation.ValidPhoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VetRegisterRequest extends RegisterRequest{

    @ValidPhoto
    private MultipartFile photo;

    @NotBlank(message = "Clinic name cannot be empty")
    private String clinicName;

    @NotNull(message = "Please select a city")
    private CityName city;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @URL(message = "Invalid URL format")
    private String site;

}
