package com.ivangeorgiev.wintergamesmanager.data.repositories;

import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SlalomResultRepository extends JpaRepository<SlalomResult, Long> {
    List<SlalomResult> findByCompetition(SlalomCompetition competition);

    Optional<SlalomResult> findByCompetitionAndAthlete(SlalomCompetition competition, Athlete athlete);
}
