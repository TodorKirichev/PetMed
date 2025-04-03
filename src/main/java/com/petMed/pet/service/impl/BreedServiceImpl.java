package com.petMed.pet.service.impl;

import com.petMed.exception.InvalidSpeciesException;
import com.petMed.pet.model.*;
import com.petMed.pet.service.BreedService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BreedServiceImpl implements BreedService {
    public List<String> getBreedsBySpecies(String species) {

        return switch (species.toUpperCase()) {
            case "CAT" -> Arrays.stream(CatBreed.values())
                    .map(CatBreed::getBreedName)
                    .toList();
            case "DOG" -> Arrays.stream(DogBreed.values())
                    .map(DogBreed::getBreedName)
                    .toList();
            case "RABBIT" -> Arrays.stream(RabbitBreed.values())
                    .map(RabbitBreed::getBreedName)
                    .toList();
            case "PARROT" -> Arrays.stream(ParrotBreed.values())
                    .map(ParrotBreed::getBreedName)
                    .toList();
            case "HAMSTER" -> Arrays.stream(HamsterBreed.values())
                    .map(HamsterBreed::getBreedName)
                    .toList();
            default -> throw new InvalidSpeciesException("Invalid species");
        };
    }
}