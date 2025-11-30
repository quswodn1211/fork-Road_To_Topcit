package com.opsw.backend.dto.usersubjectprogress.request;

public record UserSubjectProgressQuery(
        Long userId,
        Long subjectId
) {
}
