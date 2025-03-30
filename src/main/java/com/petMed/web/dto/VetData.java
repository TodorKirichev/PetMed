package com.petMed.web.dto;

import com.petMed.clinic.model.CityName;
import com.petMed.validation.UniquePhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VetData {

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Phone number cannot be empty")
    @UniquePhoneNumber
    @Pattern(regexp = "^[0-9]+$", message = "The phone number must contain only numbers")
    @Size(max = 20, message = "The phone number cannot exceed 20 characters")
    private String phone;

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
