package com.petMed.pet.model;

public enum RabbitBreed {
    HIMALAYAN("Himalayan"),
    ANGORA("Angora"),
    HOLLAND_LOP("Holland Lop"),
    MINI_REX("Mini Rex"),
    DUTCH("Dutch"),
    FLEMISH_GIANT("Flemish Giant"),
    MINI_LOP("Mini Lop"),
    ENGLISH_ANGORA("English Angora"),
    LIONHEAD("Lionhead"),
    AMERICAN_SABLE("American Sable"),
    ENGLISH_SPOT("English Spot");

    private final String breedName;

    RabbitBreed(String breedName) {
        this.breedName = breedName;
    }

    public String getBreedName() {
        return breedName;
    }
}
