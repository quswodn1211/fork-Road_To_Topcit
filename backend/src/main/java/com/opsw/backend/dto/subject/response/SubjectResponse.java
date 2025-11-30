package com.opsw.backend.dto.subject.response;

import com.opsw.backend.domain.Subject;

public record SubjectResponse(
        Long id,
        String name
) {

    public static SubjectResponse toDto(Subject subject) {
        if (subject == null) {
            return null;
        }
        return new SubjectResponse(subject.getId(), subject.getName());
    }
}
