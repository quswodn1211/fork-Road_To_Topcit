package com.opsw.backend.dto.stage.request;

public record StageAssignStoryRequest(
        Long stageId,
        Long storyId
) {
}
