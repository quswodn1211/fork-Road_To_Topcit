package com.opsw.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionResponseDto {

    private Long id;
    private String content;
    private List<String> options;
    private String answer;
    private String qtype;
    private String difficulty;
    private Long subjectId;
    private List<String> tags;
}
