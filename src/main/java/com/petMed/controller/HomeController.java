package com.petMed.controller;

import com.petMed.security.AuthenticationDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView showIndexPage() {
        return new ModelAndView("index");
    }

    @GetMapping("/home")
    public ModelAndView showHomePage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        return new ModelAndView("home");
    }
}