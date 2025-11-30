package com.opsw.backend.service;

import com.opsw.backend.domain.user.User;
import com.opsw.backend.dto.user.request.*;
import com.opsw.backend.dto.user.request.UserIdRequest;
import com.opsw.backend.dto.user.response.UserListResponse;
import com.opsw.backend.dto.user.response.UserResponse;
import com.opsw.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public UserResponse registerUser(UserRegisterRequest request) {
        User user = User.create(request.userId(), request.password());
        userRepository.save(user);
        return UserResponse.toDto(user);
    }

    @Transactional
    public UserResponse changePassword(UserChangePasswordRequest request) {
        User user = userRepository.findByLoginId(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을수 없습니다"));
        if (!user.getPassword().equals(request.oldPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        user.changePassword(request.newPassword());
        return UserResponse.toDto(user);
    }

    @Transactional
    public void removeUser(UserRemoveRequest request) {
        userRepository.deleteById(request.id());
    }

    public UserResponse getUser(UserIdRequest request) {
        return UserResponse.toDto(
                userRepository.findById(request.id())
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을수 없습니다"))
        );
    }

    public UserResponse getUserByLoginId(UserLoginIdRequest request) {
        return UserResponse.toDto(
                userRepository.findByLoginId(request.loginId())
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을수 없습니다"))
        );
    }

    public UserListResponse listUsers() {
        return UserListResponse.toDto(userRepository.findAll());
    }

    public void checkDuplicatedUserId(UserLoginIdRequest request) {
        if (userRepository.existsByLoginId(request.loginId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다");
        }
    }
}
