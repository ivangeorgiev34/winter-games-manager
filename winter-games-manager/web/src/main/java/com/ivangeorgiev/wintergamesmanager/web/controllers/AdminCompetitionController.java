package com.ivangeorgiev.wintergamesmanager.web.controllers;

import com.ivangeorgiev.wintergamesmanager.core.service.CompetitionService;
import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomCompetition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/competitions")
@RequiredArgsConstructor
public class AdminCompetitionController {

    private final CompetitionService competitionService;

    @GetMapping("")
    public String listCompetitions(Model model) {
        model.addAttribute("competitions", competitionService.findAll());
        return "admin/competitions";
    }

    @GetMapping("/new/slalom")
    public String createSlalomForm(Model model) {
        model.addAttribute("competition", new SlalomCompetition());
        return "admin/slalom-form";
    }

    @PostMapping("/new/slalom")
    public String saveSlalom(@ModelAttribute SlalomCompetition competition) {
        competitionService.save(competition);
        return "redirect:/admin/competitions";
    }

    @GetMapping("/new/biathlon")
    public String createBiathlonForm(Model model) {
        model.addAttribute("competition", new BiathlonCompetition());
        return "admin/biathlon-form";
    }

    @PostMapping("/new/biathlon")
    public String saveBiathlon(@ModelAttribute BiathlonCompetition competition) {
        competitionService.save(competition);
        return "redirect:/admin/competitions";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        competitionService.delete(id);
        return "redirect:/admin/competitions";
    }
}