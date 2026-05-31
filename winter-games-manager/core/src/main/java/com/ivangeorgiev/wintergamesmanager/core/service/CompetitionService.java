package com.ivangeorgiev.wintergamesmanager.core.service;

import com.ivangeorgiev.wintergamesmanager.data.models.Competition;
import com.ivangeorgiev.wintergamesmanager.data.repositories.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepository competitionRepository;

    public List<Competition> findAll() {
        return competitionRepository.findAllByOrderByEventDateDesc();
    }

    public Competition findById(Long id) {
        return competitionRepository.findById(id).orElseThrow(() -> new RuntimeException("Competition not found"));
    }

    public Competition save(Competition competition) {
        return competitionRepository.save(competition);
    }

    public void delete(Long id) {
        competitionRepository.deleteById(id);
    }
}
