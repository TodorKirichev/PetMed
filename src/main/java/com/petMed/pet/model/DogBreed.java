package com.petMed.pet.model;

public enum DogBreed {
    LABRADOR("Labrador"),
    BEAGLE("Beagle"),
    BULLDOG("Bulldog"),
    GERMAN_SHEPHERD("German Shepherd"),
    PUG("Pug"),
    GOLDEN_RETRIEVER("Golden Retriever"),
    FRENCH_BULLDOG("French Bulldog"),
    CHIHUAHUA("Chihuahua");

    private final String breedName;

    DogBreed(String breedName) {
        this.breedName = breedName;
    }

    public String getBreedName() {
        return breedName;
    }
}