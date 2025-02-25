package com.petMed.service;

import com.petMed.model.entity.Appointment;
import com.petMed.model.entity.User;
import com.petMed.model.enums.Role;
import com.petMed.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class UserDashboardService {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public UserDashboardService(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    public ModelAndView getDashboardView(CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findById(currentUser.getUserId());

        if (currentUser.getRole() == Role.VET) {
            modelAndView.setViewName("vet-owner-home");

            List<Appointment> appointments = appointmentService.findAllAppointmentsForToday(user);
            modelAndView.addObject("appointments", appointments);
            return modelAndView;

        } else if (currentUser.getRole() == Role.ADMIN) {
            return new ModelAndView("admin-home");
        }
        return new ModelAndView("redirect:/");
    }
}
