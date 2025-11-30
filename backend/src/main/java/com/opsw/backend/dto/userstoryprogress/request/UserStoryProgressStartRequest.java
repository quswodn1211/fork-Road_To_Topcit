package com.opsw.backend.dto.userstoryprogress.request;

public record UserStoryProgressStartRequest(
        Long userId,
        Long worldId,
        int stageLife
) {
}
