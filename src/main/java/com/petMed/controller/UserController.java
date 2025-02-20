package com.petMed.controller;

import com.petMed.model.entity.Pet;
import com.petMed.model.entity.User;
import com.petMed.security.AuthenticationDetails;
import com.petMed.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/pets")
    public ModelAndView showUserPets(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView("user-pets");

        User user = userService.findById(authenticationDetails.getUserId());
        List<Pet> pets = user.getPets();
        modelAndView.addObject("pets", pets);

        return modelAndView;
    }


}