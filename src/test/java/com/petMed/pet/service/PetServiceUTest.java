package com.petMed.pet.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.exception.PetNotFoundException;
import com.petMed.pet.model.Pet;
import com.petMed.pet.repository.PetRepository;
import com.petMed.user.model.User;
import com.petMed.web.dto.PetData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceUTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private PetService petService;

    private UUID petId;
    private UUID userId;
    private Pet pet;

    @BeforeEach
    void setUp() {
        petId = UUID.randomUUID();
        userId = UUID.randomUUID();
        pet = new Pet();
    }

    @Test
    void findPetByIdAndOwnerId_Success() {
        pet.setId(petId);
        when(petRepository.findByIdAndOwnerId(petId, userId)).thenReturn(Optional.of(pet));

        Pet result = petService.findPetByIdAndOwnerId(petId, userId);

        assertEquals(pet.getId(), result.getId());
        verify(petRepository, times(1)).findByIdAndOwnerId(petId, userId);
    }

    @Test
    void findPetByIdAndOwnerId_ShouldThrow_WhenNotFound() {
        when(petRepository.findByIdAndOwnerId(petId, userId)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.findPetByIdAndOwnerId(petId, userId));
    }

    @Test
    void delete_Success() {
        Appointment appointment = new Appointment();
        pet.getAppointments().add(appointment);

        petService.delete(pet);

        verify(petRepository, times(1)).delete(pet);
    }

    @Test
    void save_Success() {
        PetData petData = new PetData();
        petData.setSpecies("CAT");
        petData.setName("Timo");
        petData.setBreed("SCOTTISH FOLD");
        petData.setDateOfBirth(LocalDate.of(2024, 2, 23));

        User user = new User();
        String imageUrl = "imageUrl";

        petService.save(petData, user, imageUrl);

        verify(petRepository, times(1)).save(any());
    }
}
