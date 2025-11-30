package com.opsw.backend.dto.userstoryprogress.request;

public record UserStoryProgressRefillRequest(
        Long userId,
        Long worldId,
        int stageLife
) {
}
