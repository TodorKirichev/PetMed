package com.petMed.web;

import com.petMed.appointment.model.Appointment;
import com.petMed.appointment.service.AppointmentService;
import com.petMed.mapper.VetMapper;
import com.petMed.security.CurrentUser;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import com.petMed.web.dto.VetData;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("vetData")
    public VetData vetData() {
        return new VetData();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ModelAndView getUsers() {
        ModelAndView modelAndView = new ModelAndView();

        List<User> users = userService.findAllUsers();
        modelAndView.setViewName("admin-home");
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{username}/change-role")
    public ModelAndView changeRole(@PathVariable String username, @RequestParam("new-role") String newRole) {
        userService.changeRole(username, newRole);
        return new ModelAndView("redirect:/users");
    }

    @PreAuthorize("hasRole('VET')")
    @GetMapping("/profile")
    public ModelAndView editProfileForm(@AuthenticationPrincipal CurrentUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("vet-edit-profile");

        User user = userService.findById(currentUser.getUserId());
        VetData vetData = VetMapper.mapUserToVetData(user);
        modelAndView.addObject("vetData", vetData);

        return modelAndView;
    }

    @PreAuthorize("hasRole('VET')")
    @PatchMapping("/profile")
    public ModelAndView editProfile(@Valid VetData vetData, BindingResult bindingResult, @AuthenticationPrincipal CurrentUser currentUser) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("vet-edit-profile");
        }
        userService.updateVetProfile(currentUser.getUserId(), vetData);
        return new ModelAndView("redirect:/home");
    }

}