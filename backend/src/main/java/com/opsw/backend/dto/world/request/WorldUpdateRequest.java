package com.opsw.backend.dto.world.request;

public record WorldUpdateRequest(
        Long worldId,
        String name,
        Long subjectId
) {
}
