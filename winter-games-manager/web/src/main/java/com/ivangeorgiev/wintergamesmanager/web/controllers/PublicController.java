package com.ivangeorgiev.wintergamesmanager.web.controllers;

import com.ivangeorgiev.wintergamesmanager.core.service.BiathlonRankingService;
import com.ivangeorgiev.wintergamesmanager.core.service.CompetitionService;
import com.ivangeorgiev.wintergamesmanager.core.service.MedalService;
import com.ivangeorgiev.wintergamesmanager.core.service.SlalomRankingService;
import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomCompetition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final CompetitionService competitionService;
    private final SlalomRankingService slalomRankingService;
    private final BiathlonRankingService biathlonRankingService;
    private final MedalService medalService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/competitions")
    public String listCompetitions(Model model) {
        model.addAttribute("competitions", competitionService.findAll());
        return "competitions/list";
    }

    @GetMapping("/rankings/{competitionId}")
    public String showRanking(@PathVariable Long competitionId, Model model) {
        var comp = competitionService.findById(competitionId);
        model.addAttribute("competition", comp);
        if (comp instanceof SlalomCompetition) {
            System.out.println(slalomRankingService.computeFinalRanking((SlalomCompetition) comp).size());
            model.addAttribute("rankings", slalomRankingService.computeFinalRanking((SlalomCompetition) comp));
            return "rankings/slalom";
        } else if (comp instanceof BiathlonCompetition) {
            model.addAttribute("rankings", biathlonRankingService.computeRanking((BiathlonCompetition) comp));
            return "rankings/biathlon";
        }
        return "error";
    }

    @GetMapping("/medals")
    public String medalStandings(Model model) {
        model.addAttribute("standings", medalService.getMedalStandings());
        model.addAttribute("avgAge", medalService.getAverageAgeOfParticipants());
        model.addAttribute("youngest", medalService.getYoungestMedalist());
        model.addAttribute("oldest", medalService.getOldestMedalist());
        return "medals/standings";
    }
}
