package com.opsw.backend.dto.subject.request;

public record SubjectUpdateRequest(
        Long subjectId,
        String name
) {
}
