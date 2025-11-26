package com.opsw.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionAiGenerateRequest {
    private Long subjectId;
    private String difficulty;
}
