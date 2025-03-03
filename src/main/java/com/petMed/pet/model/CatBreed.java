package com.petMed.pet.model;

public enum CatBreed {
    PERSIAN("Persian"),
    MAINE_COON("Maine Coon"),
    SIAMESE("Siamese"),
    BENGAL("Bengal"),
    RAGDOLL("Ragdoll"),
    BRITISH_SHORTHAIR("British Shorthair"),
    SPHYNX("Sphynx"),
    ABYSSINIAN("Abyssinian"),
    SCOTTISH_FOLD("Scottish Fold"),
    RUSSIAN_BLUE("Russian Blue"),
    BURMESE("Burmese"),
    NORWEGIAN_FOREST_CAT("Norwegian Forest Cat"),
    TONKINESE("Tonkinese"),
    ORIENTAL_SHORTHAIR("Oriental Shorthair"),
    EGYPTIAN_MAU("Egyptian Mau");

    private final String breedName;

    CatBreed(String breedName) {
        this.breedName = breedName;
    }

    public String getBreedName() {
        return breedName;
    }
}
