package com.petMed.pet.service;

import com.petMed.web.dto.PetData;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;

import java.util.UUID;

public interface PetService {

    void save(PetData petData, User user);

    void delete(Pet pet);

    Pet findPetByIdAndOwnerId(UUID id, UUID userId);

}
