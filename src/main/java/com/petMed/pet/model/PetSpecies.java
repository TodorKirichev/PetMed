package com.petMed.pet.model;

public enum PetSpecies {
    DOG("Dog"),
    CAT("Cat"),
    RABBIT("Rabbit"),
    PARROT("Parrot"),
    HAMSTER("Hamster");

    private final String speciesName;

    PetSpecies(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getSpeciesName() {
        return speciesName;
    }
}