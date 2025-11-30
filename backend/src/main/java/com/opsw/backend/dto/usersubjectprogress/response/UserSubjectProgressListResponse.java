package com.opsw.backend.dto.usersubjectprogress.response;

import com.opsw.backend.domain.user.UserSubjectProgress;

import java.util.List;
import java.util.stream.Collectors;

public record UserSubjectProgressListResponse(
        List<UserSubjectProgressResponse> progresses
) {

    public static UserSubjectProgressListResponse toDto(List<UserSubjectProgress> progresses) {
        List<UserSubjectProgressResponse> items = progresses == null
                ? List.of()
                : progresses.stream()
                .map(UserSubjectProgressResponse::toDto)
                .collect(Collectors.toList());
        return new UserSubjectProgressListResponse(items);
    }
}
