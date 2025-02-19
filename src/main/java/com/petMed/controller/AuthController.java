package com.petMed.controller;

import com.petMed.model.dto.LoginRequest;
import com.petMed.model.dto.RegisterRequest;
import com.petMed.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("registerRequest")
    public RegisterRequest registerRequest() {
        return new RegisterRequest();
    }

    @ModelAttribute("loginRequest")
    public LoginRequest loginRequest() {
        return new LoginRequest();
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        return new ModelAndView("choose-role");
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String errorParam) {
        ModelAndView modelAndView = new ModelAndView("login");
        if (errorParam != null) {
            modelAndView.addObject("errorMessage", "Invalid username or password");
        }
        return modelAndView;
    }
}
