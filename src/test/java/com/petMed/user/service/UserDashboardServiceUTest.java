package com.petMed.user.service;

import com.petMed.clinic.model.Clinic;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDashboardServiceUTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserDashboardService userDashboardService;

    private UUID currentUserId;

    @BeforeEach
    public void setUp() {
        currentUserId = UUID.randomUUID();
    }

    @Test
    void getDashboardView_ShouldReturnVetHomePage() {
        CurrentUser currentUser = new CurrentUser(currentUserId, "vet", "123", Role.VET, true);
        Clinic clinic = new Clinic();
        User vet = new User();
        vet.setClinic(clinic);
        when(userService.findById(any())).thenReturn(vet);

        ModelAndView result = userDashboardService.getDashboardView(currentUser);

        assertEquals("redirect:/vets/schedule", result.getViewName());
    }

    @Test
    void getDashboardView_ShouldReturnVetEditProfilePage_WhenNoClinic() {
        CurrentUser currentUser = new CurrentUser(currentUserId, "vet", "123", Role.VET, true);
        User vet = new User();
        when(userService.findById(any())).thenReturn(vet);

        ModelAndView result = userDashboardService.getDashboardView(currentUser);

        assertEquals("redirect:/users/profile", result.getViewName());
    }

    @Test
    void getDashboardView_ShouldReturnAdminHomePage() {
        CurrentUser currentUser = new CurrentUser(currentUserId, "admin", "123", Role.ADMIN, true);

        ModelAndView result = userDashboardService.getDashboardView(currentUser);

        assertEquals("redirect:/users", result.getViewName());
    }

    @Test
    void getDashboardView_ShouldReturnIndexPage_WhenUserIsPetOwner() {
        CurrentUser currentUser = new CurrentUser(currentUserId, "owner", "123", Role.PET_OWNER, true);

        ModelAndView result = userDashboardService.getDashboardView(currentUser);

        assertEquals("redirect:/", result.getViewName());
    }
}
