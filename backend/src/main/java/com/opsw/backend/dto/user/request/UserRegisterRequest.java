package com.opsw.backend.dto.user.request;

public record UserRegisterRequest(
        String userId,
        String password
) {
}
