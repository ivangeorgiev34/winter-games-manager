package com.ivangeorgiev.wintergamesmanager.data.repositories;

import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.AthleteCompetitionRegistration;
import com.ivangeorgiev.wintergamesmanager.data.models.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AthleteCompetitionRegistrationRepository extends JpaRepository<AthleteCompetitionRegistration, Long> {
    List<AthleteCompetitionRegistration> findByAthlete(Athlete athlete);
    boolean existsByAthleteAndCompetition(Athlete athlete, Competition competition);

    void deleteByAthlete(Athlete athlete);
}
