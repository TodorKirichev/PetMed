package com.petMed.controller;

import com.petMed.model.dto.PetData;
import com.petMed.model.entity.Pet;
import com.petMed.model.entity.User;
import com.petMed.security.AuthenticationDetails;
import com.petMed.service.PetService;
import com.petMed.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final UserService userService;

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @ModelAttribute("petData")
    public PetData petData() {
        return new PetData();
    }

    @GetMapping("/add")
    public ModelAndView showPetForm() {
        return new ModelAndView("pet-form");
    }

    @PostMapping("/add")
    public ModelAndView addPet(@Valid PetData petData, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("pet-form");
        }
        User user = userService.findById(authenticationDetails.getUserId());

        petService.save(petData, user);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/{id}/edit-profile")
    public ModelAndView editProfile(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView("pet-edit-profile");

        Pet pet = petService.findPetByIdAndOwnerId(id, authenticationDetails.getUserId());
        modelAndView.addObject("pet", pet);

        return modelAndView;
    }

    @GetMapping("/{id}/delete")
    public ModelAndView deletePet(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        petService.delete(id, authenticationDetails.getUserId());
        return new ModelAndView("redirect:/user/pets");
    }

}
