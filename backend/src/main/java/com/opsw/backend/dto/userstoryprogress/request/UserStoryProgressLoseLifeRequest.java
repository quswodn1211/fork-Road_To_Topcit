package com.opsw.backend.dto.userstoryprogress.request;

public record UserStoryProgressLoseLifeRequest(
        Long userId,
        Long worldId
) {
}
