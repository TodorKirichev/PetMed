package com.petMed.pet.service.impl;

import com.petMed.appointment.service.AppointmentService;
import com.petMed.cloudinary.CloudinaryService;
import com.petMed.exception.PetNotFoundException;
import com.petMed.pet.model.PetSpecies;
import com.petMed.pet.service.PetService;
import com.petMed.web.dto.PetData;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;
import com.petMed.pet.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final AppointmentService appointmentService;
    private final CloudinaryService cloudinaryService;

    public PetServiceImpl(PetRepository petRepository, AppointmentService appointmentService, CloudinaryService cloudinaryService) {
        this.petRepository = petRepository;
        this.appointmentService = appointmentService;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public void save(PetData petData, User user) {
        String imageUrl = cloudinaryService.uploadFile(petData.getPhoto());
        Pet pet = create(petData, user, imageUrl);
        petRepository.save(pet);
    }

    private Pet create(PetData petData, User user, String imageUrl) {
        PetSpecies petSpecies = PetSpecies.valueOf(petData.getSpecies());
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
