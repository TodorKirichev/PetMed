package com.petMed.web.dto;

import com.petMed.clinic.model.CityName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VetData {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phone;

    @NotBlank
    private String clinicName;

    @NotNull
    private CityName city;

    @NotBlank
    private String address;

    private String site;
}
