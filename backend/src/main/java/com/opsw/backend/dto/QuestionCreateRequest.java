package com.opsw.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionCreateRequest {

    private String content;
    private List<String> options;
    private String answer;
    private String qtype;
    private String difficulty;
    private Long subjectId;
    private List<String> tags;
}
