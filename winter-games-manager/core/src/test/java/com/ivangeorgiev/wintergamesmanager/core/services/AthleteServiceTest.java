package com.ivangeorgiev.wintergamesmanager.core.services;

import com.ivangeorgiev.wintergamesmanager.core.exception.ResourceNotFoundException;
import com.ivangeorgiev.wintergamesmanager.core.service.AthleteService;
import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.AthleteCompetitionRegistration;
import com.ivangeorgiev.wintergamesmanager.data.models.SlalomCompetition;
import com.ivangeorgiev.wintergamesmanager.data.models.User;
import com.ivangeorgiev.wintergamesmanager.data.repositories.AthleteCompetitionRegistrationRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.AthleteRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.CompetitionRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AthleteServiceTest {

    @Mock private AthleteRepository athleteRepository;
    @Mock private CompetitionRepository competitionRepository;
    @Mock private AthleteCompetitionRegistrationRepository registrationRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private AthleteService athleteService;

    private Athlete athlete;
    private SlalomCompetition competition;

    @BeforeEach
    void setUp() {
        athlete = new Athlete();
        athlete.setId(1L);
        athlete.setName("John");
        athlete.setCountry("USA");
        athlete.setGender(Athlete.Gender.MALE);
        athlete.setDateOfBirth(LocalDate.of(2000, 1, 1)); // age ~25

        competition = new SlalomCompetition();
        competition.setId(1L);
        competition.setMinAge(18);
        competition.setGender(Athlete.Gender.MALE);
    }

    @Test
    void canRegisterForCompetition_shouldReturnTrue_whenAgeAndGenderMatchAndNotRegistered() {
        when(registrationRepository.existsByAthleteAndCompetition(athlete, competition)).thenReturn(false);
        boolean result = athleteService.canRegisterForCompetition(athlete, competition);
        assertThat(result).isTrue();
    }

    @Test
    void canRegisterForCompetition_shouldReturnFalse_whenAgeTooYoung() {
        athlete.setDateOfBirth(LocalDate.now().minusYears(17));
        boolean result = athleteService.canRegisterForCompetition(athlete, competition);
        assertThat(result).isFalse();
    }

    @Test
    void canRegisterForCompetition_shouldReturnFalse_whenGenderMismatch() {
        competition.setGender(Athlete.Gender.FEMALE);
        boolean result = athleteService.canRegisterForCompetition(athlete, competition);
        assertThat(result).isFalse();
    }

    @Test
    void canRegisterForCompetition_shouldReturnFalse_whenAlreadyRegistered() {
        when(registrationRepository.existsByAthleteAndCompetition(athlete, competition)).thenReturn(true);
        boolean result = athleteService.canRegisterForCompetition(athlete, competition);
        assertThat(result).isFalse();
    }

    @Test
    void registerForCompetition_shouldSaveRegistration_whenValid() {
        when(registrationRepository.existsByAthleteAndCompetition(athlete, competition)).thenReturn(false);
        athleteService.registerForCompetition(athlete, competition);
        verify(registrationRepository).save(any(AthleteCompetitionRegistration.class));
    }

    @Test
    void registerForCompetition_shouldThrow_whenInvalid() {
        when(registrationRepository.existsByAthleteAndCompetition(athlete, competition)).thenReturn(true);
        assertThatThrownBy(() -> athleteService.registerForCompetition(athlete, competition))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void findByUsername_shouldReturnAthlete_whenUserExists() {
        User user = new User();
        user.setAthlete(athlete);
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        Athlete found = athleteService.findByUsername("john");
        assertThat(found).isEqualTo(athlete);
    }

    @Test
    void findByUsername_shouldThrow_whenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> athleteService.findByUsername("unknown"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}