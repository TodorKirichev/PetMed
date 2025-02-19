package com.petMed.controller;

import com.petMed.model.dto.RegisterRequest;
import com.petMed.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("registerRequest")
    public RegisterRequest registerRequest() {
        return new RegisterRequest();
    }

    @GetMapping("/register/owner")
    public ModelAndView showRegisterForm() {
        return new ModelAndView("register-owner");
    }

    @PostMapping("/register/owner")
    public ModelAndView registerOwner(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-owner");
        }
        userService.registerOwner(registerRequest);

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/register/vet")
    public ModelAndView registerVet() {
        return new ModelAndView("register-vet");
    }
}