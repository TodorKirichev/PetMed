package com.petMed.controller;

import com.petMed.model.entity.User;
import com.petMed.security.CurrentUser;
import com.petMed.service.AppointmentService;
import com.petMed.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public AppointmentController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @PostMapping("/generate")
    public ModelAndView generateDailySchedule(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam LocalDate date, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/home");

        User vet = userService.findById(currentUser.getUserId());
        String message = appointmentService.createDailySchedule(vet, date);
        redirectAttributes.addFlashAttribute("message", message);

        return modelAndView;
    }
}
