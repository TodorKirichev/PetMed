package com.petMed.controller;

import com.petMed.model.dto.LoginRequest;
import com.petMed.model.dto.RegisterRequest;
import com.petMed.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("loginRequest")
    public LoginRequest loginRequest() {
        return new LoginRequest();
    }

    @ModelAttribute("registerRequest")
    public RegisterRequest registerRequest() {
        return new RegisterRequest();
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        return new ModelAndView("choose-role");
    }

    @GetMapping("/register-owner")
    public ModelAndView registerOwnerForm() {
        return new ModelAndView("register-owner");
    }

    @PostMapping("/register-owner")
    public ModelAndView registerOwner(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-owner");
        }
        userService.registerOwner(registerRequest);

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/register-vet")
    public ModelAndView registerVetForm() {
        return new ModelAndView("register-vet");
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
