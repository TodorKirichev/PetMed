package com.petMed.model.dto;

import com.petMed.model.enums.PetSpecies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetData {

    @NotBlank
    private String name;

    @NotNull
    private String species;

    @NotNull
    private String breed;

    @Positive
    private int age;
}
