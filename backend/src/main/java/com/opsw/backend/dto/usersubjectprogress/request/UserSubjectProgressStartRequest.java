package com.opsw.backend.dto.usersubjectprogress.request;

public record UserSubjectProgressStartRequest(
        Long userId,
        Long subjectId
) {
}
