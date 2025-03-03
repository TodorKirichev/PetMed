package com.petMed.web;

import com.petMed.user.model.Role;
import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ModelAndView getUsers() {
        ModelAndView modelAndView = new ModelAndView();

        List<User> users = userService.findAllUsers();
        modelAndView.setViewName("admin-home");
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @PostMapping("/users/{username}/change-role")
    public ModelAndView changeRole(@PathVariable String username, @RequestParam("new-role") String newRole) {
        userService.changeRole(username, newRole);
        return new ModelAndView("redirect:/admin/users");
    }
}
