package com.petMed.web;

import com.petMed.security.CurrentUser;
import com.petMed.user.model.Role;
import com.petMed.user.service.UserDashboardService;
import com.petMed.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerApiTest {

    @MockitoBean
    private UserDashboardService userDashboardService;
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private UUID currentUserId;

    @BeforeEach
    public void setup() {
        currentUserId = UUID.randomUUID();
    }

    @Test
    void showIndexPage_Success() throws Exception {
        MockHttpServletRequestBuilder request = get("/");
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"));
    }

    @Test
    void showHomePage_ShouldRedirectToVetHomePage_WhenUserIsVet() throws Exception {
        CurrentUser currentUser = new CurrentUser(currentUserId,"vet", "123", Role.VET, true);
        when(userDashboardService.getDashboardView(currentUser)).thenReturn(new ModelAndView("redirect:/vets/schedule"));

        mockMvc.perform(get("/home")
                        .with(user(currentUser)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vets/schedule"));
        verify(userDashboardService, times(1)).getDashboardView(currentUser);
    }

    @Test
    void showHomePage_ShouldRedirectToHomePage_WhenUserIsPetOwner() throws Exception {
        CurrentUser currentUser = new CurrentUser(currentUserId,"owner", "123", Role.PET_OWNER, true);
        when(userDashboardService.getDashboardView(currentUser)).thenReturn(new ModelAndView("redirect:/"));

        mockMvc.perform(get("/home")
                .with(user(currentUser)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(userDashboardService, times(1)).getDashboardView(currentUser);
    }

    @Test
    void showHomePage_ShouldRedirectToAdminHomePage_WhenUserIsAdmin() throws Exception {
        CurrentUser currentUser = new CurrentUser(currentUserId,"admin", "123", Role.ADMIN, true);
        when(userDashboardService.getDashboardView(currentUser)).thenReturn(new ModelAndView("redirect:/users"));

        mockMvc.perform(get("/home")
                .with(user(currentUser)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userDashboardService, times(1)).getDashboardView(currentUser);

    }
}
