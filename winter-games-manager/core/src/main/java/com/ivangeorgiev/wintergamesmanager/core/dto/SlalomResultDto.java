package com.ivangeorgiev.wintergamesmanager.core.dto;

import lombok.Data;

@Data
public class SlalomResultDto {
    private Long athleteId;
    private Double run1Time;
    private boolean dnfRun1;
    private Double run2Time;
    private boolean dnfRun2;
}