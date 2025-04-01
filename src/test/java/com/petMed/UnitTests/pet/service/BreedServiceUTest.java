package com.petMed.UnitTests.pet.service;

import com.petMed.pet.model.*;
import com.petMed.pet.service.BreedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BreedServiceUTest {

    @InjectMocks
    private BreedService breedService;

    @Test
    void getBreedsBySpecies_Cat_Success() {
        List<String> catBreeds = breedService.getBreedsBySpecies("CAT");

        assertTrue(catBreeds.contains(CatBreed.SCOTTISH_FOLD.getBreedName()));
    }

    @Test
    void getBreedsBySpecies_Dog_Success() {
        List<String> dogBreeds = breedService.getBreedsBySpecies("DOG");

        assertTrue(dogBreeds.contains(DogBreed.LABRADOR.getBreedName()));
    }

    @Test
    void getBreedsBySpecies_Hamster_Success() {
        List<String> dogBreeds = breedService.getBreedsBySpecies("HAMSTER");

        assertTrue(dogBreeds.contains(HamsterBreed.CHINESE.getBreedName()));
    }

    @Test
    void getBreedsBySpecies_Parrot_Success() {
        List<String> dogBreeds = breedService.getBreedsBySpecies("PARROT");

        assertTrue(dogBreeds.contains(ParrotBreed.COCKATIEL.getBreedName()));
    }

    @Test
    void getBreedsBySpecies_Rabbit_Success() {
        List<String> dogBreeds = breedService.getBreedsBySpecies("RABBIT");

        assertTrue(dogBreeds.contains(RabbitBreed.LIONHEAD.getBreedName()));
    }
}
