package com.ivangeorgiev.wintergamesmanager.data.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlalomResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Athlete athlete;

    @ManyToOne
    private SlalomCompetition competition;

    private Double run1Time;   // seconds
    private Double run2Time;
    private Double totalTime;

    private boolean dnfRun1;
    private boolean dnfRun2;
}
