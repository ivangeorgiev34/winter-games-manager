package com.ivangeorgiev.wintergamesmanager.data.repositories;

import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BiathlonResultRepository extends JpaRepository<BiathlonResult, Long> {
    List<BiathlonResult> findByCompetitionAndFinishedTrue(BiathlonCompetition competition);

    Optional<BiathlonResult> findByCompetitionAndAthlete(BiathlonCompetition competition, Athlete athlete);

    List<BiathlonResult> findByCompetition(BiathlonCompetition competition);
}