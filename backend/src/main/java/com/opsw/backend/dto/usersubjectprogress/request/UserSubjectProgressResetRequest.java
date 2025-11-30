package com.opsw.backend.dto.usersubjectprogress.request;

public record UserSubjectProgressResetRequest(
        Long userId,
        Long subjectId
) {
}
