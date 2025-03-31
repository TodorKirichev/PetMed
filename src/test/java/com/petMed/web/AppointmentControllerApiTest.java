package com.petMed.web;

import com.petMed.appointment.service.AppointmentService;
import com.petMed.pet.model.Pet;
import com.petMed.pet.service.PetService;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import com.petMed.web.dto.AppointmentData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerApiTest {

    @MockitoBean
    private AppointmentService appointmentService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private PetService petService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showAppointmentForm_ShouldReturnAppointmentForm() throws Exception {
        CurrentUser currentUser = new CurrentUser(UUID.randomUUID(),"owner", "123", Role.PET_OWNER, true);

        User vet = new User();
        User owner = new User();

        when(userService.findByUsername(any())).thenReturn(vet);
        when(userService.findById(any())).thenReturn(owner);

        mockMvc.perform(get("/appointments/book/username")
                        .with(user(currentUser)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("appointment-form"))
                .andExpect(model().attributeExists("vet"))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void bookAppointment_Success() throws Exception {
        CurrentUser currentUser = new CurrentUser(UUID.randomUUID(),"owner", "123", Role.PET_OWNER, true);
        User vet = new User();

        Pet pet = new Pet();

        AppointmentData appointmentData = new AppointmentData();
        appointmentData.setDate(LocalDate.now());
        appointmentData.setTime(LocalTime.now());
        appointmentData.setReason("reason");
        appointmentData.setPetId(UUID.randomUUID());

        when(userService.findByUsername(any())).thenReturn(vet);
        when(petService.findPetByIdAndOwnerId(any(), any())).thenReturn(pet);
        doNothing().when(appointmentService).book(any(), any(), any());

        mockMvc.perform(post("/appointments/book/username")
                .with(user(currentUser))
                        .flashAttr("appointmentData", appointmentData)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void bookAppointment_ShouldRedirectBackWhenInvalidData() throws Exception {
        CurrentUser currentUser = new CurrentUser(UUID.randomUUID(),"owner", "123", Role.PET_OWNER, true);
        User vet = new User();
        Pet pet = new Pet();
        User owner = new User();

        AppointmentData appointmentData = new AppointmentData();

        when(userService.findByUsername(any())).thenReturn(vet);
        when(petService.findPetByIdAndOwnerId(any(), any())).thenReturn(pet);
        when(userService.findById(any())).thenReturn(owner);
        doNothing().when(appointmentService).book(any(), any(), any());

        mockMvc.perform(post("/appointments/book/username")
                        .with(user(currentUser))
                        .flashAttr("appointmentData", appointmentData)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("appointment-form"));
    }
}
