package com.ivangeorgiev.wintergamesmanager.data.repositories;

import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonCompetition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiathlonCompetitionRepository extends JpaRepository<BiathlonCompetition, Long> {}
