package com.petMed.web;

import com.petMed.medicalRecord.model.MedicalRecord;
import com.petMed.web.dto.PetData;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;
import com.petMed.security.CurrentUser;
import com.petMed.pet.service.PetService;
import com.petMed.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/pets")
@PreAuthorize("hasRole('PET_OWNER')")
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

    @GetMapping
    public ModelAndView showUserPets(@AuthenticationPrincipal CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("user-pets");

        User user = userService.findById(currentUser.getUserId());
        List<Pet> pets = user.getPets();
        modelAndView.addObject("pets", pets);

        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showPetForm() {
        return new ModelAndView("pet-form");
    }

    @PostMapping("/add")
    public ModelAndView addPet(@Valid PetData petData, BindingResult bindingResult, @AuthenticationPrincipal CurrentUser currentUser) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("pet-form");
        }
        User user = userService.findById(currentUser.getUserId());

        petService.save(petData, user);

        return new ModelAndView("redirect:/pets");
    }

    @DeleteMapping("/{id}/delete")
    public ModelAndView deletePet(@PathVariable UUID id, @AuthenticationPrincipal CurrentUser currentUser) {
        petService.delete(id, currentUser.getUserId());
        return new ModelAndView("redirect:/pets");
    }

    @GetMapping("/{id}/medical-records")
    public ModelAndView getMedicalRecords(@PathVariable UUID id, @AuthenticationPrincipal CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("medical-records");

        Pet pet = petService.findPetByIdAndOwnerId(id, currentUser.getUserId());
        List<MedicalRecord> medicalRecords = pet.getMedicalRecords();
        modelAndView.addObject("medicalRecords", medicalRecords);

        return modelAndView;

    }

}
