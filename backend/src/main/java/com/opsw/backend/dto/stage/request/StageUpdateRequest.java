package com.opsw.backend.dto.stage.request;

public record StageUpdateRequest(
        Long stageId,
        int stageNumber
) {
}
