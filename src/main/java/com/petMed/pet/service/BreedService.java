package com.petMed.pet.service;

import com.petMed.pet.model.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BreedService {
    public List<String> getBreedsBySpecies(String species) {

        switch (species.toUpperCase()) {
            case "CAT":
                return Arrays.stream(CatBreed.values())
                        .map(breed -> breed.getBreedName())
                        .toList();
            case "DOG":
                return Arrays.stream(DogBreed.values())
                        .map(breed -> breed.getBreedName())
                        .toList();
            case "RABBIT":
                return Arrays.stream(RabbitBreed.values())
                        .map(breed -> breed.getBreedName())
                        .toList();
            case "PARROT":
                return Arrays.stream(ParrotBreed.values())
                        .map(breed -> breed.getBreedName())
                        .toList();
            case "HAMSTER":
                return Arrays.stream(HamsterBreed.values())
                        .map(breed -> breed.getBreedName())
                        .toList();
            default:
                throw new RuntimeException("Invalid species");
        }
    }
}