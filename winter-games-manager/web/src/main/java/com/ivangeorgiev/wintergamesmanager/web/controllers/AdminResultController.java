package com.ivangeorgiev.wintergamesmanager.web.controllers;

import com.ivangeorgiev.wintergamesmanager.core.dto.BiathlonResultDto;
import com.ivangeorgiev.wintergamesmanager.core.dto.SlalomResultDto;
import com.ivangeorgiev.wintergamesmanager.core.dto.SlalomResultDtoList;
import com.ivangeorgiev.wintergamesmanager.core.exception.ResourceNotFoundException;
import com.ivangeorgiev.wintergamesmanager.core.service.AthleteService;
import com.ivangeorgiev.wintergamesmanager.core.service.CompetitionService;
import com.ivangeorgiev.wintergamesmanager.core.service.ResultService;
import com.ivangeorgiev.wintergamesmanager.core.service.SlalomRankingService;
import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomResult;
import com.ivangeorgiev.wintergamesmanager.data.repositories.SlalomResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/results")
@RequiredArgsConstructor
public class AdminResultController {

    private final CompetitionService competitionService;
    private final AthleteService athleteService;
    private final ResultService resultService;
    private final SlalomRankingService slalomRankingService;
    private final SlalomResultRepository slalomResultRepository;

    @GetMapping("/slalom/{id}/run1")
    public String showRun1Form(@PathVariable Long id, Model model) {
        SlalomCompetition comp = (SlalomCompetition) competitionService.findById(id);
        List<Athlete> athletes = comp.getRegisteredAthletes();

        List<SlalomResultDto> dtos = new ArrayList<>();
        for (Athlete a : athletes) {
            SlalomResultDto dto = new SlalomResultDto();
            dto.setAthleteId(a.getId());

            Optional<SlalomResult> existing = slalomResultRepository.findByCompetitionAndAthlete(comp, a);
            if (existing.isPresent()) {
                SlalomResult res = existing.get();
                dto.setRun1Time(res.getRun1Time());
                dto.setDnfRun1(res.isDnfRun1());
            }
            dtos.add(dto);
        }
        SlalomResultDtoList resultList = new SlalomResultDtoList();
        resultList.setResults(dtos);

        model.addAttribute("competition", comp);
        model.addAttribute("athletes", athletes);
        model.addAttribute("resultList", resultList);
        return "admin/slalom-run1";
    }

    @PostMapping("/slalom/{id}/run1")
    public String saveRun1(@PathVariable Long id,
                           @ModelAttribute SlalomResultDtoList resultList) {
        SlalomCompetition comp = (SlalomCompetition) competitionService.findById(id);
        System.out.println("=== Received ResultList size: " + resultList.getResults().size());
        for (SlalomResultDto dto : resultList.getResults()) {

            System.out.println("DTO: athleteId=" + dto.getAthleteId() +
                    ", time=" + dto.getRun1Time() +
                    ", dnf=" + dto.isDnfRun1());
            if (dto.getAthleteId() != null) {
                Athlete athlete = athleteService.findById(dto.getAthleteId());
                resultService.saveSlalomRun1(comp, athlete, dto.getRun1Time(), dto.isDnfRun1());
            }
        }
        return "redirect:/admin/results/slalom/" + id + "/qualifiers";
    }

    @GetMapping("/slalom/{id}/qualifiers")
    public String showQualifiers(@PathVariable Long id, Model model) {
        SlalomCompetition comp = (SlalomCompetition) competitionService.findById(id);
        model.addAttribute("competition", comp);
        model.addAttribute("qualifiers", slalomRankingService.getSecondRunQualifiers(comp));
        List<SlalomResult> qualifyingResults = slalomRankingService.getQualifyingResults(comp);
        model.addAttribute("qualifyingResults", qualifyingResults);
        return "admin/slalom-qualifiers";
    }

    @GetMapping("/biathlon/{id}/enter")
    public String enterBiathlon(@PathVariable Long id, Model model) {
        BiathlonCompetition comp = (BiathlonCompetition) competitionService.findById(id);
        model.addAttribute("competition", comp);
        model.addAttribute("resultDto", new BiathlonResultDto());

        List<Athlete> registeredAthletes = comp.getRegisteredAthletes();
        model.addAttribute("athletes", registeredAthletes);

        return "admin/biathlon-result";
    }

    @PostMapping("/biathlon/{id}/enter")
    public String saveBiathlon(@PathVariable Long id, @ModelAttribute BiathlonResultDto dto) {
        BiathlonCompetition comp = (BiathlonCompetition) competitionService.findById(id);
        Athlete athlete = athleteService.findById(dto.getAthleteId());
        resultService.saveBiathlonResult(comp, athlete, dto.getSkiingTimeSeconds(), dto.getMisses(), dto.isFinished());
        return "redirect:/rankings/" + id;
    }

    @GetMapping("/slalom/{compId}/run2")
    public String showRun2Form(@PathVariable Long compId,
                               @RequestParam Long athleteId,
                               Model model) {
        SlalomCompetition comp = (SlalomCompetition) competitionService.findById(compId);
        Athlete athlete = athleteService.findById(athleteId);

        SlalomResult existingResult = slalomResultRepository.findByCompetitionAndAthlete(comp, athlete)
                .orElseThrow(() -> new ResourceNotFoundException("No Run 1 result found for this athlete"));

        SlalomResultDto dto = new SlalomResultDto();
        dto.setAthleteId(athleteId);
        dto.setRun1Time(existingResult.getRun1Time()); // for display only

        model.addAttribute("competition", comp);
        model.addAttribute("athlete", athlete);
        model.addAttribute("result", existingResult);   // to show run1 time
        model.addAttribute("resultDto", dto);
        return "admin/slalom-run2";
    }

    @PostMapping("/slalom/{compId}/run2")
    public String saveRun2(@PathVariable Long compId,
                           @ModelAttribute SlalomResultDto resultDto) {
        SlalomCompetition comp = (SlalomCompetition) competitionService.findById(compId);
        Athlete athlete = athleteService.findById(resultDto.getAthleteId());
        resultService.saveSlalomRun2(comp, athlete, resultDto.getRun2Time(), resultDto.isDnfRun2());
        return "redirect:/rankings/" + compId;
    }
}