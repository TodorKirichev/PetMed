package com.petMed.web;

import com.petMed.user.model.User;
import com.petMed.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView showIndexPage() {
        ModelAndView modelAndView = new ModelAndView("index");

        List<User> vets = userService.findAllVets();
        modelAndView.addObject("vets", vets);

        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView showHomePage() {
        return new ModelAndView("redirect:/");
    }
}