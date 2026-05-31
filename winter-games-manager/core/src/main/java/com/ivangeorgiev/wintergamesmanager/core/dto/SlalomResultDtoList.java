package com.ivangeorgiev.wintergamesmanager.core.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class SlalomResultDtoList {
    private List<SlalomResultDto> results = new ArrayList<>();
}