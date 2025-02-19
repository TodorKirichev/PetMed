package com.petMed.controller;

import com.petMed.model.dto.PetData;
import com.petMed.model.entity.User;
import com.petMed.security.AuthenticationDetails;
import com.petMed.service.PetService;
import com.petMed.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    @PostMapping("add")
    public ModelAndView addPet(@Valid PetData petData, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("pet-form");
        }
        User user = userService.findById(authenticationDetails.getUserId());

        petService.save(petData, user);

        return new ModelAndView("redirect:/home");
    }
}
