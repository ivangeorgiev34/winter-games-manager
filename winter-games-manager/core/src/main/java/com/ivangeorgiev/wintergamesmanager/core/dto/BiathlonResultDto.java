package com.ivangeorgiev.wintergamesmanager.core.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class BiathlonResultDto {
    private Long athleteId;
    private Long competitionId;

    @Positive
    private double skiingTimeSeconds;
    @Min(0)
    private int misses;
    private boolean finished;
}
