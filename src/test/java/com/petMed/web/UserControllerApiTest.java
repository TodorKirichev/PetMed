package com.petMed.web;

import com.petMed.clinic.model.CityName;
import com.petMed.clinic.model.Clinic;
import com.petMed.mapper.VetMapper;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import com.petMed.web.dto.VetData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private VetMapper vetMapper;

    @Autowired
    private MockMvc mockMvc;

    private UUID userId;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
    }

    @Test
    void editProfileForm_ShouldReturnEditProfileForm() throws Exception {
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
                .phone("123456789")
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
    @WithMockUser(roles = "ADMIN")
    void getUsers_ShouldReturnAdminHomePage() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(new User()));

        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("admin-home"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeRole_AsAdmin_ShouldSucceed() throws Exception {
        String username = "username";
        String newRole = "PET_OWNER";

        mockMvc.perform(post("/users/username/change-role")
                        .param("new-role", newRole))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).changeRole(username, newRole);
    }
}
