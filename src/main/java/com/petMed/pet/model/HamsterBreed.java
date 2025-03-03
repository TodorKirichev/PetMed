package com.petMed.pet.model;

public enum HamsterBreed {
    SYRIAN("Syrian"),
    DWARF_CAMPBELL_RUSSIAN("Dwarf Campbell Russian"),
    DWARF_ROBOROVSKI("Dwarf Roborovski"),
    CHINESE("Chinese");

    private final String breedName;

    HamsterBreed(String breedName) {
        this.breedName = breedName;
    }

    public String getBreedName() {
        return breedName;
    }
}