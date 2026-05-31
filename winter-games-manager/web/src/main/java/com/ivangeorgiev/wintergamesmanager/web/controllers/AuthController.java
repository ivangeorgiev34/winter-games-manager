package com.ivangeorgiev.wintergamesmanager.web.controllers;

import com.ivangeorgiev.wintergamesmanager.core.dto.UserRegistrationDto;
import com.ivangeorgiev.wintergamesmanager.core.service.AthleteService;
import com.ivangeorgiev.wintergamesmanager.core.service.UserService;
import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AthleteService athleteService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("athlete", new Athlete());
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Athlete athlete,
                           @ModelAttribute("user") UserRegistrationDto userDto) {
        Athlete savedAthlete = athleteService.save(athlete);
        userService.registerAthlete(userDto.getUsername(), userDto.getPassword(), savedAthlete);
        return "redirect:/login";
    }
}
