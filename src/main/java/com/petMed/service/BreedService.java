package com.petMed.service;

import com.petMed.model.enums.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreedService {
    public List<Enum<?>> getBreedsBySpecies(String species) {
        if (species.equals(PetSpecies.CAT.name())) {
            return List.of(CatBreed.values());
        } else if (species.equals(PetSpecies.DOG.name())) {
            return List.of(DogBreed.values());
        } else if (species.equals(PetSpecies.RABBIT.name())) {
            return List.of(RabbitBreed.values());
        } else if (species.equals(PetSpecies.PARROT.name())) {
            return List.of(ParrotBreed.values());
        } else if (species.equals(PetSpecies.HAMSTER.name())) {
            return List.of(HamsterBreed.values());
        } else {
            return List.of();
        }
    }
}
