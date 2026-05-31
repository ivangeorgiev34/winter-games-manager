package com.ivangeorgiev.wintergamesmanager.data.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BiathlonResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Athlete athlete;

    @ManyToOne
    private BiathlonCompetition competition;

    private double skiingTimeSeconds;
    private int misses;
    private double totalTime;

    private boolean finished;
}
