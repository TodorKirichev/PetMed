package com.petMed.service;

import com.petMed.model.dto.PetData;
import com.petMed.model.entity.Pet;
import com.petMed.model.entity.User;
import com.petMed.model.enums.PetSpecies;
import com.petMed.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final AppointmentService appointmentService;

    public PetService(PetRepository petRepository, AppointmentService appointmentService) {
        this.petRepository = petRepository;
        this.appointmentService = appointmentService;
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

    public Pet findPetByIdAndOwnerId(UUID id, UUID userId) {
        return getByIdAndOwnerId(id, userId);
    }

    public void delete(UUID id, UUID userId) {
        Pet byIdAndOwnerId = getByIdAndOwnerId(id, userId);
        appointmentService.freeUp(byIdAndOwnerId.getAppointments());
        petRepository.delete(byIdAndOwnerId);
    }

    private Pet getByIdAndOwnerId(UUID id, UUID userId) {
        Optional<Pet> byIdAndOwnerId = petRepository.findByIdAndOwnerId(id, userId);

        if (byIdAndOwnerId.isEmpty()) {
            throw new RuntimeException("Pet not found");
        }
        return byIdAndOwnerId.get();
    }
}
