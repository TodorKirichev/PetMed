package com.petMed.controller;

import com.petMed.model.dto.AppointmentData;
import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.Pet;
import com.petMed.model.entity.User;
import com.petMed.security.CurrentUser;
import com.petMed.service.AppointmentService;
import com.petMed.service.PetService;
import com.petMed.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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

    @PostMapping("/generate")
    public ModelAndView generateDailySchedule(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam LocalDate date, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/home");

        User vet = userService.findById(currentUser.getUserId());
        String message = appointmentService.createDailySchedule(vet, date);
        redirectAttributes.addFlashAttribute("message", message);

        return modelAndView;
    }

    @GetMapping("/book/{username}")
    public ModelAndView showAppointmentForm(@PathVariable String username, @AuthenticationPrincipal CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("appointment-form");

        User vet = userService.findVetByUsername(username);
        User owner = userService.findById(currentUser.getUserId());

        modelAndView.addObject("vet", vet);
        modelAndView.addObject("owner", owner);

        return modelAndView;
    }

    @PostMapping("/book/{username}")
    public ModelAndView bookAppointment(@PathVariable String username, @AuthenticationPrincipal CurrentUser currentUser, AppointmentData appointmentData) {
        ModelAndView modelAndView = new ModelAndView("redirect:/home");

        User vet = userService.findVetByUsername(username);
        Pet pet = petService.findPetByIdAndOwnerId(appointmentData.getPetId(), currentUser.getUserId());

        appointmentService.book(vet, pet, appointmentData);

        return modelAndView;
    }


}
