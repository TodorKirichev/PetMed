package com.petMed.service;

import com.petMed.model.enums.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BreedService {
    public List<String> getBreedsBySpecies(String species) {

        switch (species.toUpperCase()) {
            case "CAT":
                return Arrays.stream(CatBreed.values())
                        .map(Enum::name)
                        .toList();
            case "DOG":
                return Arrays.stream(DogBreed.values())
                        .map(Enum::name)
                        .toList();
            case "RABBIT":
                return Arrays.stream(RabbitBreed.values())
                        .map(Enum::name)
                        .toList();
            case "PARROT":
                return Arrays.stream(ParrotBreed.values())
                        .map(Enum::name)
                        .toList();
            case "HAMSTER":
                return Arrays.stream(HamsterBreed.values())
                        .map(Enum::name)
                        .toList();
            default:
                throw new RuntimeException("Invalid species");
        }
    }
}