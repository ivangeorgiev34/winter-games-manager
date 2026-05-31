package com.ivangeorgiev.wintergamesmanager.core.services;

import com.ivangeorgiev.wintergamesmanager.core.service.BiathlonRankingService;
import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.BiathlonResult;
import com.ivangeorgiev.wintergamesmanager.data.repositories.BiathlonResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BiathlonRankingServiceTest {

    @Mock
    private BiathlonResultRepository biathlonResultRepository;

    @InjectMocks
    private BiathlonRankingService biathlonRankingService;

    private BiathlonCompetition competition;
    private BiathlonResult result1, result2;

    @BeforeEach
    void setUp() {
        competition = new BiathlonCompetition();
        competition.setPenaltySecondsPerMiss(60);
        competition.setId(1L);

        result1 = new BiathlonResult();
        result1.setAthlete(new Athlete());
        result1.setSkiingTimeSeconds(500.0);
        result1.setMisses(2);
        result1.setTotalTime(620.0);
        result1.setFinished(true);

        result2 = new BiathlonResult();
        result2.setAthlete(new Athlete());
        result2.setSkiingTimeSeconds(480.0);
        result2.setMisses(1);
        result2.setTotalTime(540.0);
        result2.setFinished(true);
    }

    @Test
    void calculateTotalTime_shouldAddPenaltySeconds() {
        double total = biathlonRankingService.calculateTotalTime(500.0, 3, 60);
        assertThat(total).isEqualTo(680.0);
    }
}