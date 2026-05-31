package com.ivangeorgiev.wintergamesmanager.core.service;

import com.ivangeorgiev.wintergamesmanager.core.exception.ResourceNotFoundException;
import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.AthleteCompetitionRegistration;
import com.ivangeorgiev.wintergamesmanager.data.models.Competition;
import com.ivangeorgiev.wintergamesmanager.data.models.User;
import com.ivangeorgiev.wintergamesmanager.data.repositories.AthleteCompetitionRegistrationRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.AthleteRepository;
import com.ivangeorgiev.wintergamesmanager.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AthleteService {

    private final AthleteRepository athleteRepository;
    private final UserRepository userRepository;
    private final AthleteCompetitionRegistrationRepository registrationRepository;

    public Athlete findById(Long id) {
        return athleteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Athlete not found"));
    }

    public Athlete save(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public void delete(Long id) {
        athleteRepository.deleteById(id);
    }

    public boolean canRegisterForCompetition(Athlete athlete, Competition competition) {
        int age = Period.between(athlete.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < competition.getMinAge()) return false;
        if (athlete.getGender() != competition.getGender()) return false;
        if (registrationRepository.existsByAthleteAndCompetition(athlete, competition)) return false;
        return true;
    }

    public Athlete findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getAthlete() == null) throw new ResourceNotFoundException("No athlete linked");
        return user.getAthlete();
    }

    public void registerForCompetition(Athlete athlete, Competition competition) {
        if (!canRegisterForCompetition(athlete, competition)) {
            throw new IllegalStateException("Cannot register – age/gender/duplicate");
        }
        AthleteCompetitionRegistration reg = new AthleteCompetitionRegistration();
        reg.setAthlete(athlete);
        reg.setCompetition(competition);
        registrationRepository.save(reg);
    }

    public List<AthleteCompetitionRegistration> getRegistrations(Athlete athlete) {
        return registrationRepository.findByAthlete(athlete);
    }

    @Transactional
    public void deleteAthleteAndUser(Athlete athlete) {
        User user = userRepository.findByAthlete(athlete)
                .orElseThrow(() -> new ResourceNotFoundException("User not linked"));
        registrationRepository.deleteByAthlete(athlete);
        athleteRepository.delete(athlete);
        userRepository.delete(user);
    }
}