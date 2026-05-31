package com.ivangeorgiev.wintergamesmanager.core.services;

import com.ivangeorgiev.wintergamesmanager.core.service.BiathlonRankingService;
import com.ivangeorgiev.wintergamesmanager.core.service.MedalService;
import com.ivangeorgiev.wintergamesmanager.core.service.SlalomRankingService;
import com.ivangeorgiev.wintergamesmanager.data.models.*;
import com.ivangeorgiev.wintergamesmanager.data.repositories.BiathlonResultRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.CompetitionRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.SlalomResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedalServiceTest {

    @Mock private CompetitionRepository competitionRepository;
    @Mock private SlalomResultRepository slalomResultRepository;
    @Mock private BiathlonResultRepository biathlonResultRepository;
    @Mock private SlalomRankingService slalomRankingService;
    @Mock private BiathlonRankingService biathlonRankingService;

    @InjectMocks
    private MedalService medalService;

    private SlalomCompetition slalom;
    private BiathlonCompetition biathlon;
    private Athlete athlete1, athlete2, athlete3;
    private SlalomResult slalomResult1, slalomResult2, slalomResult3;
    private BiathlonResult biathlonResult1, biathlonResult2, biathlonResult3;

    @BeforeEach
    void setUp() {
        slalom = new SlalomCompetition();
        slalom.setId(1L);
        biathlon = new BiathlonCompetition();
        biathlon.setId(2L);

        athlete1 = new Athlete(); athlete1.setId(1L); athlete1.setCountry("USA");
        athlete2 = new Athlete(); athlete2.setId(2L); athlete2.setCountry("CAN");
        athlete3 = new Athlete(); athlete3.setId(3L); athlete3.setCountry("USA");

        slalomResult1 = new SlalomResult(); slalomResult1.setAthlete(athlete1);
        slalomResult2 = new SlalomResult(); slalomResult2.setAthlete(athlete2);
        slalomResult3 = new SlalomResult(); slalomResult3.setAthlete(athlete3);

        biathlonResult1 = new BiathlonResult(); biathlonResult1.setAthlete(athlete2);
        biathlonResult2 = new BiathlonResult(); biathlonResult2.setAthlete(athlete3);
        biathlonResult3 = new BiathlonResult(); biathlonResult3.setAthlete(athlete1);
    }

    @Test
    void getMedalStandings_shouldAggregateMedalsCorrectly() {
        when(competitionRepository.findAll()).thenReturn(Arrays.asList(slalom, biathlon));
        when(slalomRankingService.computeFinalRanking(slalom)).thenReturn(Arrays.asList(slalomResult1, slalomResult2, slalomResult3));
        when(biathlonRankingService.computeRanking(biathlon)).thenReturn(Arrays.asList(biathlonResult1, biathlonResult2, biathlonResult3));

        Map<String, MedalService.MedalCount> standings = medalService.getMedalStandings();

        assertThat(standings).containsKeys("USA", "CAN");
        MedalService.MedalCount usa = standings.get("USA");
        assertThat(usa.getGold()).isEqualTo(1);
        assertThat(usa.getSilver()).isEqualTo(1);
        assertThat(usa.getBronze()).isEqualTo(2);

        MedalService.MedalCount can = standings.get("CAN");
        assertThat(can.getGold()).isEqualTo(1);
        assertThat(can.getSilver()).isEqualTo(1);
        assertThat(can.getBronze()).isEqualTo(0);
    }
}