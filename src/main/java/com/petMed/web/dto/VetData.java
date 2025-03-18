package com.petMed.web.dto;

import com.petMed.clinic.model.CityName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VetData {

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotBlank(message = "Phone number cannot be empty")
    private String phone;

    @NotBlank(message = "Clinic name cannot be empty")
    private String clinicName;

    @NotNull(message = "Please select a city")
    private CityName city;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @URL(message = "Invalid URL format")
    private String site;
}
