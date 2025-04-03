package com.petMed.pet.service;

import java.util.List;

public interface BreedService {

    List<String> getBreedsBySpecies(String species);
}