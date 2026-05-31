package com.ivangeorgiev.wintergamesmanager.core.service;

import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonResult;
import com.ivangeorgiev.wintergamesmanager.data.repositories.BiathlonResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BiathlonRankingService {

    private final BiathlonResultRepository biathlonResultRepository;

    public List<BiathlonResult> computeRanking(BiathlonCompetition competition) {
        List<BiathlonResult> results = biathlonResultRepository.findByCompetition(competition);
        return results.stream()
                .filter(r -> r.getTotalTime() < Double.MAX_VALUE)
                .sorted(Comparator.comparingDouble(BiathlonResult::getTotalTime))
                .collect(Collectors.toList());
    }

    public double calculateTotalTime(double skiingTime, int misses, int penaltyPerMiss) {
        return skiingTime + (misses * penaltyPerMiss);
    }
}