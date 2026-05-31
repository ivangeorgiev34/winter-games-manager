package com.ivangeorgiev.wintergamesmanager.data.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "competition_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Athlete.Gender gender;

    @Min(10)
    private int minAge;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventDate;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AthleteCompetitionRegistration> registrations = new ArrayList<>();

    public List<Athlete> getRegisteredAthletes() {
        return registrations.stream()
                .map(AthleteCompetitionRegistration::getAthlete)
                .collect(Collectors.toList());
    }
}