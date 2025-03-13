package com.petMed.web;

import com.petMed.cloudinary.CloudinaryService;
import com.petMed.web.dto.VetRegisterRequest;
import com.petMed.web.dto.LoginRequest;
import com.petMed.web.dto.PetOwnerRegisterRequest;
import com.petMed.user.service.UserService;
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
    private final CloudinaryService cloudinaryService;

    public AuthController(UserService userService, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    @ModelAttribute("loginRequest")
    public LoginRequest loginRequest() {
        return new LoginRequest();
    }

    @ModelAttribute("petOwnerRegisterRequest")
    public PetOwnerRegisterRequest petOwnerRegisterRequest() {
        return new PetOwnerRegisterRequest();
    }

    @ModelAttribute("vetRegisterRequest")
    public VetRegisterRequest vetRegisterRequest() {
        return new VetRegisterRequest();
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
    public ModelAndView registerOwner(@Valid PetOwnerRegisterRequest petOwnerRegisterRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-owner");
        }
        userService.registerOwner(petOwnerRegisterRequest);

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/register-vet")
    public ModelAndView registerVetForm() {
        return new ModelAndView("register-vet");
    }

    @PostMapping("/register-vet")
    public ModelAndView registerVet(@Valid VetRegisterRequest vetRegisterRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register-vet");
        }
        String imageUrl = cloudinaryService.uploadFile(vetRegisterRequest.getPhoto());
        userService.registerVet(vetRegisterRequest, imageUrl);

        return new ModelAndView("redirect:/login");
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
