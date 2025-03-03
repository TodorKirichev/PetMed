package com.petMed.pet.model;

public enum ParrotBreed {
    MACAW("Macaw"),
    AFRICAN_GREY("African Grey"),
    COCKATIEL("Cockatiel"),
    BUDGERIGAR("Budgerigar"),
    CONURE("Conure"),
    AMAZONS("Amazons"),
    LOVEBIRD("Lovebird"),
    PARROTLET("Parrotlet"),
    COCKATOOS("Cockatoos"),
    ECLECTUS("Eclectus");

    private final String breedName;

    ParrotBreed(String breedName) {
        this.breedName = breedName;
    }

    public String getBreedName() {
        return breedName;
    }
}