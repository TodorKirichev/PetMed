package com.petMed.controller;

import com.petMed.security.CurrentUser;
import com.petMed.service.UserDashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final UserDashboardService userDashboardService;

    public HomeController(UserDashboardService userDashboardService) {
        this.userDashboardService = userDashboardService;
    }

    @GetMapping("/home")
    public ModelAndView showHomePage(@AuthenticationPrincipal CurrentUser currentUser) {
        String view = userDashboardService.getDashboardView(currentUser);
        return new ModelAndView(view);
    }
}