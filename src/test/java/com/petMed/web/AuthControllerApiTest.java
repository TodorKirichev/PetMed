package com.petMed.web;

import com.petMed.user.repository.UserRepository;
import com.petMed.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showRegisterForm_Success() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("choose-role"));
    }

    @Test
    void showRegisterOwnerForm_Success() throws Exception {
        mockMvc.perform(get("/register-owner"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register-owner"))
                .andExpect(model().attributeExists("petOwnerRegisterRequest"));
    }

    @Test
    void registerOwner_Success() throws Exception {
        mockMvc.perform(post("/register-owner")
                .formField("username", "username")
                .formField("password", "password")
                .formField("firstName", "firstName")
                .formField("lastName", "lastName")
                .formField("email", "email@gmail.com")
                .formField("phone", "4654")
                .formField("confirmPassword", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).registerOwner(any());
    }

    @Test
    void registerOwner_ShouldRedirectBack_WhenInvalidData() throws Exception {
        mockMvc.perform(post("/register-owner")
                        .formField("username", "as")
                        .formField("password", "12")
                        .formField("firstName", "firstName")
                        .formField("lastName", "lastName")
                        .formField("email", "email@gmail.com")
                        .formField("phone", "4654")
                        .formField("confirmPassword", "password")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register-owner"));

        verify(userService, times(0)).registerOwner(any());
    }

    @Test
    void showRegisterVetForm_Success() throws Exception {
        mockMvc.perform(get("/register-vet"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register-vet"))
                .andExpect(model().attributeExists("vetRegisterRequest"));
    }

    @Test
    void registerVet_Success() throws Exception {
        MockMultipartFile photo = new MockMultipartFile(
                "photo",
                "test-photo.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3, 4, 5}
        );

        mockMvc.perform(multipart("/register-vet")
                        .file(photo)
                        .param("username", "username")
                        .param("password", "password")
                        .param("confirmPassword", "password")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("email", "email@gmail.com")
                        .param("phone", "4654")
                        .param("clinicName", "clinicName")
                        .param("city", "PLOVDIV")
                        .param("address", "Some Address")
                        .param("site", "https://www.site.com")
                        .with(csrf())
                        .contentType("multipart/form-data"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).registerVet(any());
    }

    @Test
    void registerVet_ShouldRedirectBack_WhenInvalidData() throws Exception {
        mockMvc.perform(multipart("/register-vet")

                        .param("username", "as")
                        .param("password", "12")
                        .param("confirmPassword", "password")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("email", "email@gmail.com")
                        .param("phone", "4654")
                        .param("clinicName", "clinicName")
                        .param("city", "PLOVDIV")
                        .param("address", "Some Address")
                        .param("site", "https://www.site.com")
                        .with(csrf())
                        .contentType("multipart/form-data"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register-vet"));

        verify(userService, times(0)).registerVet(any());
    }

    @Test
    void showLoginForm_Success() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"));
    }

    @Test
    void showLoginForm_WithErrorParam_ShouldReturnErrorMessageAttribute() throws Exception {
        mockMvc.perform(get("/login").param("error", "error"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}
