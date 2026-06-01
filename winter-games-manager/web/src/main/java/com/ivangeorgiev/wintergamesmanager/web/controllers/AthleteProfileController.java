package com.ivangeorgiev.wintergamesmanager.web.controllers;

import com.ivangeorgiev.wintergamesmanager.core.dto.CompetitionRegistrationDto;
import com.ivangeorgiev.wintergamesmanager.core.service.AthleteService;
import com.ivangeorgiev.wintergamesmanager.core.service.CompetitionService;
import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.Competition;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/athlete")
@RequiredArgsConstructor
public class AthleteProfileController {

    private final AthleteService athleteService;
    private final CompetitionService competitionService;

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Athlete athlete = athleteService.findByUsername(userDetails.getUsername());
        model.addAttribute("athlete", athlete);
        return "athlete/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Athlete athlete = athleteService.findByUsername(userDetails.getUsername());
        model.addAttribute("athlete", athlete);
        return "athlete/profile-edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @ModelAttribute Athlete updatedAthlete) {
        Athlete existing = athleteService.findByUsername(userDetails.getUsername());
        existing.setName(updatedAthlete.getName());
        existing.setCountry(updatedAthlete.getCountry());
        existing.setGender(updatedAthlete.getGender());
        existing.setDateOfBirth(updatedAthlete.getDateOfBirth());
        athleteService.save(existing);
        return "redirect:/athlete/profile";
    }

    // Delete account
    @PostMapping("/profile/delete")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        Athlete athlete = athleteService.findByUsername(userDetails.getUsername());
        athleteService.deleteAthleteAndUser(athlete);
        return "redirect:/logout";
    }

    // Show competition registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Athlete athlete = athleteService.findByUsername(userDetails.getUsername());
        List<Competition> competitions = competitionService.findAll().stream()
                .filter(a -> a.getGender() == athlete.getGender())
                .collect(Collectors.toList());

        model.addAttribute("competitions", competitions);
        model.addAttribute("registration", new CompetitionRegistrationDto());
        return "athlete/register";
    }

    // Submit competition registration
    @PostMapping("/register")
    public String registerCompetition(@ModelAttribute CompetitionRegistrationDto dto,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        Athlete athlete = athleteService.findByUsername(userDetails.getUsername());
        var competition = competitionService.findById(dto.getCompetitionId());
        athleteService.registerForCompetition(athlete, competition);
        return "redirect:/athlete/registrations";
    }

    // List athlete's registrations
    @GetMapping("/registrations")
    public String myRegistrations(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Athlete athlete = athleteService.findByUsername(userDetails.getUsername());
        model.addAttribute("registrations", athleteService.getRegistrations(athlete));
        return "athlete/registrations";
    }
}