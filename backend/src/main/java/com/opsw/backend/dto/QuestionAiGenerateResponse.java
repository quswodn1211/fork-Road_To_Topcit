package com.opsw.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionAiGenerateResponse {
    private Long id;
    private String content;
    private List<String> options;
    private String answer;
    private String difficulty;
}
