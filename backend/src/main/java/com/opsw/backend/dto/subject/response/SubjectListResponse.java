package com.opsw.backend.dto.subject.response;

import com.opsw.backend.domain.Subject;

import java.util.List;
import java.util.stream.Collectors;

public record SubjectListResponse(
        List<SubjectResponse> subjects
) {

    public static SubjectListResponse toDto(List<Subject> subjects) {
        List<SubjectResponse> items = subjects == null
                ? List.of()
                : subjects.stream()
                .map(SubjectResponse::toDto)
                .collect(Collectors.toList());
        return new SubjectListResponse(items);
    }
}
