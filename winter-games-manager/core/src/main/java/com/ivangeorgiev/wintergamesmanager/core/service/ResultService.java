package com.ivangeorgiev.wintergamesmanager.core.service;

import com.ivangeorgiev.wintergamesmanager.data.models.*;
import com.ivangeorgiev.wintergamesmanager.data.repositories.BiathlonResultRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.SlalomResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final SlalomResultRepository slalomResultRepository;
    private final BiathlonResultRepository biathlonResultRepository;
    private final SlalomRankingService slalomRankingService;
    private final BiathlonRankingService biathlonRankingService;

    @Transactional
    public void saveSlalomRun1(SlalomCompetition competition, Athlete athlete, Double time, boolean dnf) {
        SlalomResult result = findOrCreateSlalomResult(competition, athlete);
        result.setRun1Time(time);
        result.setDnfRun1(dnf);
        if (dnf) result.setRun2Time(null);
        slalomRankingService.updateTotalTime(result);
        slalomResultRepository.save(result);
    }

    @Transactional
    public void saveSlalomRun2(SlalomCompetition competition, Athlete athlete, Double time, boolean dnf) {
        SlalomResult result = findOrCreateSlalomResult(competition, athlete);
        result.setRun2Time(time);
        result.setDnfRun2(dnf);
        slalomRankingService.updateTotalTime(result);
        slalomResultRepository.save(result);
    }

    private SlalomResult findOrCreateSlalomResult(SlalomCompetition competition, Athlete athlete) {
        return slalomResultRepository.findByCompetitionAndAthlete(competition, athlete)
                .orElseGet(() -> {
                    SlalomResult r = new SlalomResult();
                    r.setCompetition(competition);
                    r.setAthlete(athlete);
                    return r;
                });
    }

    @Transactional
    public void saveBiathlonResult(BiathlonCompetition competition, Athlete athlete, double skiingTime, int misses, boolean finished) {
        BiathlonResult result = biathlonResultRepository.findByCompetitionAndAthlete(competition, athlete)
                .orElse(new BiathlonResult());
        result.setCompetition(competition);
        result.setAthlete(athlete);
        result.setSkiingTimeSeconds(skiingTime);
        result.setMisses(misses);
        result.setFinished(finished);
        if (finished) {
            double total = biathlonRankingService.calculateTotalTime(skiingTime, misses, competition.getPenaltySecondsPerMiss());
            result.setTotalTime(total);
        } else {
            result.setTotalTime(Double.MAX_VALUE);
        }
        biathlonResultRepository.save(result);
    }
}
