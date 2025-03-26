package com.petMed.web;

import com.petMed.clinic.model.CityName;
import com.petMed.clinic.model.Clinic;
import com.petMed.mapper.VetMapper;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import com.petMed.web.dto.VetData;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private VetMapper vetMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void editProfileForm_ShouldReturnEditProfileForm() throws Exception {
        UUID userId = UUID.randomUUID();
        CurrentUser currentUser = new CurrentUser(userId,"vet", "123", Role.VET, true);
        Clinic clinic = Clinic.builder()
                .name("petMed")
                .city(CityName.PLOVDIV)
                .address("Ivan Vazov")
                .site("www.petMed.com")
                .build();
        User vet = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .phone("phone")
                .clinic(clinic)
                .build();
        when(userService.findById(any())).thenReturn(vet);
        try (MockedStatic<VetMapper> mockedMapper = mockStatic(VetMapper.class)) {
            mockedMapper.when(() -> VetMapper.mapUserToVetData(vet)).thenReturn(new VetData());
        }

        mockMvc.perform(get("/users/profile")
                .with(user(currentUser)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("vet-edit-profile"))
                .andExpect(model().attributeExists("vetData"));
    }

    @Test
    void editProfile_Success() throws Exception {
        UUID userId = UUID.randomUUID();
        CurrentUser currentUser = new CurrentUser(userId,"vet", "123", Role.VET, true);
        VetData vetData = VetData.builder()
                .firstName("firstName")
                .lastName("lastName")
                .phone("phone")
                .clinicName("clinic")
                .city(CityName.PLOVDIV)
                .address("Ivan Vazov")
                .site("http://petMed.com")
                .build();

        doNothing().when(userService).updateVetProfile(eq(userId), any(VetData.class));

        mockMvc.perform(patch("/users/profile")
                .with(user(currentUser))
                .flashAttr("vetData", vetData))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void editProfile_ShouldRedirectBack_WhenInvalidData() throws Exception {
        UUID userId = UUID.randomUUID();
        CurrentUser currentUser = new CurrentUser(userId,"vet", "123", Role.VET, true);
        VetData vetData = VetData.builder()
                .firstName("firstName")
                .lastName("as")
                .phone("phone")
                .clinicName("clinic")
                .city(CityName.PLOVDIV)
                .address("Ivan Vazov")
                .site("petMed.com")
                .build();

        doNothing().when(userService).updateVetProfile(eq(userId), any(VetData.class));

        mockMvc.perform(patch("/users/profile")
                        .with(user(currentUser))
                        .flashAttr("vetData", vetData))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("vet-edit-profile"));
    }
}
