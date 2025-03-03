package com.petMed.user.service;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.user.model.User;
import com.petMed.user.model.Role;
import com.petMed.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserDashboardService {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public UserDashboardService(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    public ModelAndView  getDashboardView(CurrentUser currentUser) {
        if (currentUser.getRole() == Role.VET) {
            User vet = userService.findById(currentUser.getUserId());
            if (vet.getClinic() == null) {
                return new ModelAndView("redirect:/vets/profile");
            }
            return new ModelAndView("redirect:/vets/schedule");

        } else if (currentUser.getRole() == Role.ADMIN) {
            return new ModelAndView("redirect:/admin/users");
        }
        return new ModelAndView("redirect:/");
    }
}
