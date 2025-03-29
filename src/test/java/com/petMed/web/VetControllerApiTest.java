package com.petMed.web;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VetController.class)
public class VetControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AppointmentService appointmentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSchedule_ShouldReturnVetSchedulePage() throws Exception {
        CurrentUser currentUser = new CurrentUser(UUID.randomUUID(),"vet", "123", Role.VET, true);
        User vet = new User();
        List<Appointment> appointments = List.of(new Appointment());
        when(userService.findById(any())).thenReturn(vet);
        when(appointmentService.findAllAppointmentsByDayAndVet(any(), any())).thenReturn(appointments);

        mockMvc.perform(get("/vets/schedule")
                        .with(user(currentUser)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("vet-home"))
                .andExpect(model().attributeExists("vet"))
                .andExpect(model().attributeExists("appointments"));
    }

    @Test
    void searchVets_ShouldReturnIndexPageWithVets() throws Exception {
        when(userService.findVets(any(), any())).thenReturn(List.of(new User()));

        mockMvc.perform(get("/vets/search"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("vets"));
    }
}
