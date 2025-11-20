package com.opsw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodayRecommendResponse {

    private List<ProblemItem> recommendedProblems;
    private String reason;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProblemItem {
        private Long id;
        private Long subjectId;
        private String difficulty;
    }
}
