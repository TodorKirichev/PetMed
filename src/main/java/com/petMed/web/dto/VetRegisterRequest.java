package com.petMed.web.dto;

import com.petMed.clinic.model.CityName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VetRegisterRequest extends RegisterRequest{

    @NotBlank
    private String clinicName;

    @NotNull
    private CityName city;

    @NotBlank
    private String address;

    private String site;
}
