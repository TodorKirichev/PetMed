package com.petMed.web;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.mapper.VetMapper;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import com.petMed.web.dto.VetData;
import com.petMed.web.dto.VetRegisterRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vets")
@PreAuthorize("hasRole('VET')")
public class VetController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    public VetController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @ModelAttribute("vetData")
    public VetData vetData() {
        return new VetData();
    }

    @GetMapping("/schedule")
    public ModelAndView getSchedule(@AuthenticationPrincipal CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("vet-home");

        User vet = userService.findById(currentUser.getUserId());
        List<Appointment> appointments = appointmentService.findAllAppointmentsByDayAndVet(LocalDate.now(), vet);

        modelAndView.addObject("appointments", appointments);
        modelAndView.addObject("vet", vet);

        return modelAndView;
    }

    @GetMapping("/profile")
    public ModelAndView editProfileForm(@AuthenticationPrincipal CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("vet-edit-profile");

        User user = userService.findById(currentUser.getUserId());
        VetData vetData = VetMapper.mapUserToVetData(user);
        modelAndView.addObject("vetData", vetData);

        return modelAndView;
    }

    @PostMapping("/profile")
    public ModelAndView editProfile(@Valid VetData vetData, BindingResult bindingResult, @AuthenticationPrincipal CurrentUser currentUser) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("vet-edit-profile");
        }
        userService.updateVetProfile(currentUser.getUserId(), vetData);
        return new ModelAndView("redirect:/home");
    }
}
