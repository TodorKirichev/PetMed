package com.petMed.ApiTests.web;

import com.petMed.cloudinary.CloudinaryService;
import com.petMed.pet.model.Pet;
import com.petMed.pet.model.PetSpecies;
import com.petMed.pet.service.PetService;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import com.petMed.web.PetController;
import com.petMed.web.dto.PetData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerApiTest {

    @MockitoBean
    private PetService petService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private CloudinaryService cloudinaryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deletePet_Success() throws Exception {
        CurrentUser currentUser = new CurrentUser(UUID.randomUUID(),"owner", "123", Role.PET_OWNER, true);
        Pet pet = new Pet();
        when(petService.findPetByIdAndOwnerId(any(), any())).thenReturn(pet);
        doNothing().when(petService).delete(any());

        mockMvc.perform(delete("/pets/8914a649-8824-487e-bc6c-759cc7c48aac/delete")
                        .with(user(currentUser))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pets"));
    }

    @Test
    @WithMockUser(roles = "PET_OWNER")
    void showPetForm_ShouldReturnPetForm() throws Exception {
        mockMvc.perform(get("/pets/add"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("pet-form"));
    }

    @Test
    void addPet_Success() throws Exception {
        CurrentUser currentUser = new CurrentUser(UUID.randomUUID(),"owner", "123", Role.PET_OWNER, true);

        MockMultipartFile photo = new MockMultipartFile(
                "photo",
                "test-photo.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3, 4, 5}
        );

        PetData petData = new PetData();
        petData.setName("pet");
        petData.setSpecies("cat");
        petData.setBreed("scottish");
        petData.setDateOfBirth(LocalDate.of(2024, 2 ,11));
        petData.setPhoto(photo);

        User user = new User();
        when(userService.findById(any())).thenReturn(user);
        when(cloudinaryService.uploadFile(any())).thenReturn("success");
        doNothing().when(petService).save(any(), any(), any());

        mockMvc.perform(post("/pets/add")
                        .with(user(currentUser))
                        .flashAttr("petData", petData)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pets"));
    }

    @Test
    void addPet_ShouldRedirectBackWhenInvalidData() throws Exception {
        CurrentUser currentUser = new CurrentUser(UUID.randomUUID(),"owner", "123", Role.PET_OWNER, true);

        PetData petData = new PetData();

        mockMvc.perform(post("/pets/add")
                        .with(user(currentUser))
                        .flashAttr("petData", petData)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("pet-form"));
    }

    @Test
    void showUserPets_ShouldReturnUserPetsPage() throws Exception {
        CurrentUser currentUser = new CurrentUser(UUID.randomUUID(),"owner", "123", Role.PET_OWNER, true);

        User user = new User();
        Pet pet = new Pet();
        pet.setName("pet");
        pet.setSpecies(PetSpecies.CAT);
        pet.setBreed("scottish");
        pet.setDateOfBirth(LocalDate.of(2024, 2, 11));
        pet.setImageUrl("http://pet-image");
        user.getPets().add(pet);
        when(userService.findById(any())).thenReturn(user);

        mockMvc.perform(get("/pets")
                        .with(user(currentUser)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user-pets"))
                .andExpect(model().attributeExists("pets"));
    }
}
