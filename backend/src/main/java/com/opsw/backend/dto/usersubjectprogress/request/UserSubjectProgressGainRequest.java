package com.opsw.backend.dto.usersubjectprogress.request;

public record UserSubjectProgressGainRequest(
        Long userId,
        Long subjectId,
        int amount
) {
}
