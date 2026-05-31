package com.ivangeorgiev.wintergamesmanager.data.repositories;

import com.ivangeorgiev.wintergamesmanager.data.models.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findAllByOrderByEventDateDesc();
}