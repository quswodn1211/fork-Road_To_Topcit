package com.opsw.backend.dto.userstoryprogress.request;

public record UserStoryProgressAdvanceRequest(
        Long userId,
        Long worldId,
        Long stageId
) {
}
