package com.petMed.web;

import com.petMed.web.dto.AppointmentData;
import com.petMed.pet.model.Pet;
import com.petMed.user.model.User;
import com.petMed.security.CurrentUser;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.pet.service.PetService;
import com.petMed.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final PetService petService;

    public AppointmentController(AppointmentService appointmentService, UserService userService, PetService petService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.petService = petService;
    }

    @ModelAttribute("appointmentData")
    public AppointmentData appointmentData() {
        return new AppointmentData();
    }

    @GetMapping("/book/{username}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ModelAndView showAppointmentForm(@PathVariable String username, @AuthenticationPrincipal CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("appointment-form");

        User vet = userService.findByUsername(username);
        User owner = userService.findById(currentUser.getUserId());

        modelAndView.addObject("vet", vet);
        modelAndView.addObject("owner", owner);

        return modelAndView;
    }

    @PostMapping("/book/{username}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ModelAndView bookAppointment(@Valid AppointmentData appointmentData, BindingResult bindingResult, @PathVariable String username, @AuthenticationPrincipal CurrentUser currentUser) {
        User vet = userService.findByUsername(username);

        if (bindingResult.hasErrors()) {
            User owner = userService.findById(currentUser.getUserId());
            ModelAndView modelAndView = new ModelAndView("appointment-form");
            modelAndView.addObject("vet", vet);
            modelAndView.addObject("owner", owner);
            return modelAndView;
        }
        Pet pet = petService.findPetByIdAndOwnerId(appointmentData.getPetId(), currentUser.getUserId());

        appointmentService.book(vet, pet, appointmentData);

        return new ModelAndView("redirect:/home");
    }


}
