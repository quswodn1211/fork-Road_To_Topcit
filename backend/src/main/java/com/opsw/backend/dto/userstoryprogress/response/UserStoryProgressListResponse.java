package com.opsw.backend.dto.userstoryprogress.response;

import com.opsw.backend.domain.user.UserStoryProgress;

import java.util.List;
import java.util.stream.Collectors;

public record UserStoryProgressListResponse(
        List<UserStoryProgressResponse> progresses
) {

    public static UserStoryProgressListResponse toDto(List<UserStoryProgress> progresses) {
        List<UserStoryProgressResponse> items = progresses == null
                ? List.of()
                : progresses.stream()
                .map(UserStoryProgressResponse::toDto)
                .collect(Collectors.toList());
        return new UserStoryProgressListResponse(items);
    }
}
