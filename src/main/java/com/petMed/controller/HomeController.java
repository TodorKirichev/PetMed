package com.petMed.controller;

import com.petMed.model.entity.User;
import com.petMed.security.CurrentUser;
import com.petMed.service.UserDashboardService;
import com.petMed.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    private final UserDashboardService userDashboardService;
    private final UserService userService;

    public HomeController(UserDashboardService userDashboardService, UserService userService) {
        this.userDashboardService = userDashboardService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView showIndexPage() {
        ModelAndView modelAndView = new ModelAndView("index");

        List<User> vets = userService.findAllVets();
        modelAndView.addObject("vets", vets);

        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView showHomePage(@AuthenticationPrincipal CurrentUser currentUser) {
        return userDashboardService.getDashboardView(currentUser);
    }
}