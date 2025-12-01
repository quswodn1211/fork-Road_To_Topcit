package com.opsw.backend.dto.user.response;

import com.opsw.backend.domain.user.User;

public record UserResponse(
        Long id,
        String userId
) {

    public static UserResponse toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getLoginId()
        );
    }
}
