package com.ivangeorgiev.wintergamesmanager.core.service;

import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomResult;
import com.ivangeorgiev.wintergamesmanager.data.repositories.SlalomResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlalomRankingService {

    private final SlalomResultRepository slalomResultRepository;

    public List<SlalomResult> computeFinalRanking(SlalomCompetition competition) {
        List<SlalomResult> results = slalomResultRepository.findByCompetition(competition);
        System.out.println("results: " + results.stream()
                .filter(r -> !r.isDnfRun1() && !r.isDnfRun2() && r.getRun1Time() != null && r.getRun2Time() != null)
                .sorted(Comparator.comparingDouble(SlalomResult::getTotalTime))
                .collect(Collectors.toList()).size());
        return results.stream()
                .filter(r -> !r.isDnfRun1() && !r.isDnfRun2() && r.getRun1Time() != null && r.getRun2Time() != null)
                .sorted(Comparator.comparingDouble(SlalomResult::getTotalTime))
                .collect(Collectors.toList());
    }

    public List<Athlete> getSecondRunQualifiers(SlalomCompetition competition) {
        List<SlalomResult> results = slalomResultRepository.findByCompetition(competition);
        List<SlalomResult> finishedRun1 = results.stream()
                .filter(r -> !r.isDnfRun1() && r.getRun1Time() != null)
                .sorted(Comparator.comparingDouble(SlalomResult::getRun1Time))
                .limit(competition.getSecondRunQualifiersCount())
                .collect(Collectors.toList());
        // reverse order for second run (slowest first)
        finishedRun1.sort(Comparator.comparingDouble(SlalomResult::getRun1Time).reversed());
        return finishedRun1.stream().map(SlalomResult::getAthlete).collect(Collectors.toList());
    }

    public void updateTotalTime(SlalomResult result) {
        if (!result.isDnfRun1() && !result.isDnfRun2()
                && result.getRun1Time() != null && result.getRun2Time() != null) {
            result.setTotalTime(result.getRun1Time() + result.getRun2Time());
        } else {
            result.setTotalTime(null);
        }
    }

    public List<SlalomResult> getQualifyingResults(SlalomCompetition competition) {
        // Get all results for this competition that have a valid Run 1 time (non‑null, not DNF)
        List<SlalomResult> allResults = slalomResultRepository.findByCompetition(competition);
        List<SlalomResult> finishedRun1 = allResults.stream()
                .filter(r -> !r.isDnfRun1() && r.getRun1Time() != null)
                .sorted(Comparator.comparingDouble(SlalomResult::getRun1Time))
                .limit(competition.getSecondRunQualifiersCount())
                .collect(Collectors.toList());

        // Second run starts with the slowest first (descending Run 1 time)
        finishedRun1.sort(Comparator.comparingDouble(SlalomResult::getRun1Time).reversed());

        return finishedRun1;
    }
}
