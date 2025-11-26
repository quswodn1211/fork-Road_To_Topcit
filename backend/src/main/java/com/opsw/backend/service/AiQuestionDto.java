package com.opsw.backend.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AiQuestionDto {
    private String content;
    private List<String> options;
    private String answer;
}
