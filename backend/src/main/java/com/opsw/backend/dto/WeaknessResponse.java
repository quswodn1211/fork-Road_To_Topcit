package com.opsw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeaknessResponse {
    private String subjectName;
    private double accuracy;
    private String weakness;
    private String recommendation;
}
