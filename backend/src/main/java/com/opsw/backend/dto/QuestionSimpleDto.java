package com.opsw.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionSimpleDto {

    private Long id;
    private String content;
    private String difficulty;
}
