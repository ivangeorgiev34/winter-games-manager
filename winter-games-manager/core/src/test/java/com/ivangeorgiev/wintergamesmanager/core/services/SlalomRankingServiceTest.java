package com.ivangeorgiev.wintergamesmanager.core.services;

import com.ivangeorgiev.wintergamesmanager.core.service.SlalomRankingService;
import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomResult;
import com.ivangeorgiev.wintergamesmanager.data.repositories.SlalomResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlalomRankingServiceTest {

    @Mock
    private SlalomResultRepository slalomResultRepository;

    @InjectMocks
    private SlalomRankingService slalomRankingService;

    private SlalomCompetition competition;
    private Athlete athlete1, athlete2, athlete3;
    private SlalomResult result1, result2, result3;

    @BeforeEach
    void setUp() {
        competition = new SlalomCompetition();
        competition.setId(1L);
        competition.setSecondRunQualifiersCount(2);

        athlete1 = new Athlete(); athlete1.setId(1L); athlete1.setName("A1");
        athlete2 = new Athlete(); athlete2.setId(2L); athlete2.setName("A2");
        athlete3 = new Athlete(); athlete3.setId(3L); athlete3.setName("A3");

        result1 = new SlalomResult(); result1.setAthlete(athlete1); result1.setRun1Time(100.0); result1.setDnfRun1(false);
        result2 = new SlalomResult(); result2.setAthlete(athlete2); result2.setRun1Time(90.0); result2.setDnfRun1(false);
        result3 = new SlalomResult(); result3.setAthlete(athlete3); result3.setRun1Time(110.0); result3.setDnfRun1(true); // DNF
    }

    @Test
    void computeFinalRanking_shouldReturnOnlyFinishedBothRunsSortedByTotalTime() {
        result1.setRun2Time(50.0); result1.setDnfRun2(false); result1.setTotalTime(150.0);
        result2.setRun2Time(60.0); result2.setDnfRun2(false); result2.setTotalTime(150.0);
        result3.setRun2Time(40.0); result3.setDnfRun2(false); result3.setTotalTime(150.0); // DNF in run1, should be excluded

        when(slalomResultRepository.findByCompetition(competition)).thenReturn(Arrays.asList(result1, result2, result3));

        List<SlalomResult> ranking = slalomRankingService.computeFinalRanking(competition);

        assertThat(ranking).hasSize(2);
        assertThat(ranking.get(0).getAthlete().getId()).isEqualTo(1L);
        result1.setTotalTime(160.0);
        result2.setTotalTime(150.0);
        when(slalomResultRepository.findByCompetition(competition)).thenReturn(Arrays.asList(result1, result2, result3));
        ranking = slalomRankingService.computeFinalRanking(competition);
        assertThat(ranking.get(0).getTotalTime()).isEqualTo(150.0);
        assertThat(ranking.get(1).getTotalTime()).isEqualTo(160.0);
    }

    @Test
    void getSecondRunQualifiers_shouldReturnTopNAthletesByRun1TimeExcludingDNF_inReverseOrder() {
        when(slalomResultRepository.findByCompetition(competition)).thenReturn(Arrays.asList(result1, result2, result3));
        List<Athlete> qualifiers = slalomRankingService.getSecondRunQualifiers(competition);
        assertThat(qualifiers).hasSize(2);
        assertThat(qualifiers.get(0).getId()).isEqualTo(1L); // slower first
        assertThat(qualifiers.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void updateTotalTime_shouldCalculateSumIfBothRunsNonNullAndNotDNF() {
        SlalomResult result = new SlalomResult();
        result.setRun1Time(10.5);
        result.setRun2Time(20.3);
        result.setDnfRun1(false);
        result.setDnfRun2(false);
        slalomRankingService.updateTotalTime(result);
        assertThat(result.getTotalTime()).isEqualTo(30.8);
    }

    @Test
    void updateTotalTime_shouldSetNullIfAnyRunDNF() {
        SlalomResult result = new SlalomResult();
        result.setRun1Time(10.5);
        result.setRun2Time(20.3);
        result.setDnfRun1(true);
        slalomRankingService.updateTotalTime(result);
        assertThat(result.getTotalTime()).isNull();
    }
}