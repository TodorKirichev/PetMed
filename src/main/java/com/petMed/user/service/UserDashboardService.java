package com.petMed.user.service;

import com.petMed.user.model.User;
import com.petMed.user.model.Role;
import com.petMed.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class UserDashboardService {

    private final UserService userService;

    public UserDashboardService(UserService userService) {
        this.userService = userService;
    }

    public ModelAndView  getDashboardView(CurrentUser currentUser) {
        if (currentUser.getRole() == Role.VET) {
            User vet = userService.findById(currentUser.getUserId());
            if (vet.getClinic() == null) {
                return new ModelAndView("redirect:/users/profile");
            }
            return new ModelAndView("redirect:/vets/schedule");

        } else if (currentUser.getRole() == Role.ADMIN) {
            return new ModelAndView("redirect:/users");
        }
        return new ModelAndView("redirect:/");
    }
}
