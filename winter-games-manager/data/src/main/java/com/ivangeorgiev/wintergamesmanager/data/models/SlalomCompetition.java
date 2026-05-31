package com.ivangeorgiev.wintergamesmanager.data.models;

import lombok.*;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SLALOM")
@Getter
@Setter
@NoArgsConstructor
public class SlalomCompetition extends Competition {
    private int secondRunQualifiersCount = 30;
}
