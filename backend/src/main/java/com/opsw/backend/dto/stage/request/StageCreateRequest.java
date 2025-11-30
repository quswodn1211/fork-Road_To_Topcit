package com.opsw.backend.dto.stage.request;

public record StageCreateRequest(
        Long worldId,
        int stageNumber
) {
}
