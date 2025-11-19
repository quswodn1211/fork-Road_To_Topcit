package com.opsw.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttemptHistoryResponse {

    private Long attemptId;
    private Long userId;
    private String submittedAnswer;
    private boolean isCorrect;
    private int gainedXp;
    private String createdAt;
}
