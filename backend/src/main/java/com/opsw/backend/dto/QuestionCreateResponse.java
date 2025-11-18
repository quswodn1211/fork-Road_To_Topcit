package com.opsw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionCreateResponse {
    private Long id;
    private String message;
}
