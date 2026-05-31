package com.ivangeorgiev.wintergamesmanager.core.service;

import com.ivangeorgiev.wintergamesmanager.data.models.*;
import com.ivangeorgiev.wintergamesmanager.data.repositories.AthleteRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.BiathlonResultRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.CompetitionRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.SlalomResultRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedalService {

    private final CompetitionRepository competitionRepository;
    private final SlalomResultRepository slalomResultRepository;
    private final BiathlonResultRepository biathlonResultRepository;
    private final SlalomRankingService slalomRankingService;
    private final BiathlonRankingService biathlonRankingService;
    private final AthleteRepository athleteRepository;

    public Map<String, MedalCount> getMedalStandings() {
        Map<String, MedalCount> standings = new HashMap<>();

        List<Competition> competitions = competitionRepository.findAll();
        for (Competition comp : competitions) {
            if (comp instanceof SlalomCompetition) {
                List<SlalomResult> ranking = slalomRankingService.computeFinalRanking((SlalomCompetition) comp);
                assignMedals(ranking.stream().map(SlalomResult::getAthlete).collect(Collectors.toList()), standings);
            } else if (comp instanceof BiathlonCompetition) {
                List<BiathlonResult> ranking = biathlonRankingService.computeRanking((BiathlonCompetition) comp);
                assignMedals(ranking.stream().map(BiathlonResult::getAthlete).collect(Collectors.toList()), standings);
            }
        }
        return standings;
    }

    private void assignMedals(List<Athlete> athletes, Map<String, MedalCount> standings) {
        if (athletes.size() >= 1) addMedal(athletes.get(0).getCountry(), "GOLD", standings);
        if (athletes.size() >= 2) addMedal(athletes.get(1).getCountry(), "SILVER", standings);
        if (athletes.size() >= 3) addMedal(athletes.get(2).getCountry(), "BRONZE", standings);
    }

    private void addMedal(String country, String medal, Map<String, MedalCount> standings) {
        standings.computeIfAbsent(country, k -> new MedalCount()).increment(medal);
    }

    public double getAverageAgeOfParticipants() {
        List<Athlete> allAthletes = athleteRepository.findAll();
        return allAthletes.stream()
                .mapToInt(a -> Period.between(a.getDateOfBirth(), LocalDate.now()).getYears())
                .average()
                .orElse(0.0);
    }

    public Athlete getYoungestMedalist() {
        Athlete youngest = getMedalistByAge(true);
        return youngest != null ? youngest : createPlaceholderAthlete();
    }

    public Athlete getOldestMedalist() {
        Athlete oldest = getMedalistByAge(false);
        return oldest != null ? oldest : createPlaceholderAthlete();
    }

    private Athlete createPlaceholderAthlete() {
        Athlete placeholder = new Athlete();
        placeholder.setName("—");
        placeholder.setCountry("—");
        return placeholder;
    }

    private Athlete getMedalistByAge(boolean youngest) {
        // Gather all medal winners
        Set<Athlete> medalists = new HashSet<>();
        List<Competition> competitions = competitionRepository.findAll();
        for (Competition comp : competitions) {
            if (comp instanceof SlalomCompetition) {
                List<SlalomResult> ranking = slalomRankingService.computeFinalRanking((SlalomCompetition) comp);
                medalists.addAll(ranking.stream().limit(3).map(SlalomResult::getAthlete).collect(Collectors.toList()));
            } else if (comp instanceof BiathlonCompetition) {
                List<BiathlonResult> ranking = biathlonRankingService.computeRanking((BiathlonCompetition) comp);
                medalists.addAll(ranking.stream().limit(3).map(BiathlonResult::getAthlete).collect(Collectors.toList()));
            }
        }
        return medalists.stream()
                .min(Comparator.comparing(Athlete::getDateOfBirth))  // youngest
                .orElse(null);
    }

    @Data
    public static class MedalCount {
        private int gold, silver, bronze = 0;
        public void increment(String medal) {
            switch (medal) {
                case "GOLD": gold++; break;
                case "SILVER": silver++; break;
                case "BRONZE": bronze++; break;
            }
        }
    }
}