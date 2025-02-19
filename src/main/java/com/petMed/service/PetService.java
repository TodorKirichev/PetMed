package com.petMed.service;

import com.petMed.model.dto.PetData;
import com.petMed.model.entity.Pet;
import com.petMed.model.entity.User;
import com.petMed.model.enums.PetSpecies;
import com.petMed.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public void save(PetData petData, User user) {
        PetSpecies petSpecies = PetSpecies.valueOf(petData.getSpecies());
        Pet pet = Pet.builder()
                .name(petData.getName())
                .species(petSpecies)
                .breed(petData.getBreed())
                .age(petData.getAge())
                .owner(user)
                .build();

        petRepository.save(pet);
    }
}
