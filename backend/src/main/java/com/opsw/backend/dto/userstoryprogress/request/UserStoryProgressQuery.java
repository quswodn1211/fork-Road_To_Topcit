package com.opsw.backend.dto.userstoryprogress.request;

public record UserStoryProgressQuery(
        Long userId,
        Long worldId
) {
}
