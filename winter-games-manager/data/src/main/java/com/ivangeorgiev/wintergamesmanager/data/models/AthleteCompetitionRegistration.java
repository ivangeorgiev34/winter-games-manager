package com.ivangeorgiev.wintergamesmanager.data.models;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"athlete_id", "competition_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AthleteCompetitionRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "athlete_id")
    private Athlete athlete;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    private LocalDateTime registrationDate = LocalDateTime.now();
}