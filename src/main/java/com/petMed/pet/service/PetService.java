package com.petMed.pet.service;

import com.petMed.appointment.service.AppointmentService;
import com.petMed.exception.PetNotFoundException;
import com.petMed.pet.model.PetSpecies;
import com.petMed.web.dto.PetData;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;
import com.petMed.pet.repository.PetRepository;
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

    public void save(PetData petData, User user, String imageUrl) {
        PetSpecies petSpecies = PetSpecies.valueOf(petData.getSpecies());
        Pet pet = create(petData, user, petSpecies, imageUrl);
        petRepository.save(pet);
    }

    private static Pet create(PetData petData, User user, PetSpecies petSpecies, String imageUrl) {
        return Pet.builder()
                .name(petData.getName())
                .species(petSpecies)
                .breed(petData.getBreed())
                .dateOfBirth(petData.getDateOfBirth())
                .imageUrl(imageUrl)
                .owner(user)
                .build();
    }

    public void delete(Pet pet) {
        appointmentService.freeUp(pet.getAppointments());
        petRepository.delete(pet);
    }

    public Pet findPetByIdAndOwnerId(UUID id, UUID userId) {
        Optional<Pet> byIdAndOwnerId = petRepository.findByIdAndOwnerId(id, userId);
        if (byIdAndOwnerId.isEmpty()) {
            throw new PetNotFoundException("Pet not found");
        }
        return byIdAndOwnerId.get();
    }

}
