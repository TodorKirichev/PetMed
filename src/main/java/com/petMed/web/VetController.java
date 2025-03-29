package com.petMed.web;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vets")
public class VetController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    public VetController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasRole('VET')")
    @GetMapping("/schedule")
    public ModelAndView getSchedule(@AuthenticationPrincipal CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("vet-home");

        User vet = userService.findById(currentUser.getUserId());
        List<Appointment> appointments = appointmentService.findAllAppointmentsByDayAndVet(LocalDate.now(), vet);

        modelAndView.addObject("appointments", appointments);
        modelAndView.addObject("vet", vet);

        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView findVets(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "city", required = false) String city) {

        ModelAndView modelAndView = new ModelAndView("index");
        List<User> vets = userService.findVets(name, city);

        modelAndView.addObject("vets", vets);

        return modelAndView;
    }
}
