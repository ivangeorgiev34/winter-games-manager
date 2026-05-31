package com.ivangeorgiev.wintergamesmanager.data.models;

import lombok.*;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BIATHLON")
@Getter
@Setter
@NoArgsConstructor
public class BiathlonCompetition extends Competition {
    private int laps;
    private int shootings;
    private int penaltySecondsPerMiss = 60;
}