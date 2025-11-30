package com.opsw.backend.dto.user.response;

import com.opsw.backend.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

public record UserListResponse(
        List<UserResponse> users
) {

    public static UserListResponse toDto(List<User> users) {
        List<UserResponse> items = users == null
                ? List.of()
                : users.stream()
                .map(UserResponse::toDto)
                .collect(Collectors.toList());
        return new UserListResponse(items);
    }
}
