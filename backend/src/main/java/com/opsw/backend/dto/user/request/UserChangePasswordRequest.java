package com.opsw.backend.dto.user.request;

public record UserChangePasswordRequest(
        String userId,
        String oldPassword,
        String newPassword
) {
}
