package com.opsw.backend.dto.world.request;

public record WorldCreateRequest(
        String name,
        Long subjectId
) {
}
