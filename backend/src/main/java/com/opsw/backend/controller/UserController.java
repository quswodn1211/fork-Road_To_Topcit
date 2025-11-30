package com.opsw.backend.controller;

import com.opsw.backend.controller.docs.UserApi;
import com.opsw.backend.dto.user.request.UserChangePasswordRequest;
import com.opsw.backend.dto.user.request.UserIdRequest;
import com.opsw.backend.dto.user.request.UserLoginIdRequest;
import com.opsw.backend.dto.user.request.UserRegisterRequest;
import com.opsw.backend.dto.user.request.UserRemoveRequest;
import com.opsw.backend.dto.user.response.UserListResponse;
import com.opsw.backend.dto.user.response.UserResponse;
import com.opsw.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @PostMapping
    @Override
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/password")
    @Override
    public ResponseEntity<UserResponse> changePassword(@RequestBody UserChangePasswordRequest request) {
        UserResponse result = userService.changePassword(request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        userService.removeUser(new UserRemoveRequest(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(new UserIdRequest(id)));
    }

    @GetMapping("/by-login/{loginId}")
    @Override
    public ResponseEntity<UserResponse> getByLoginId(@PathVariable String loginId) {
        return ResponseEntity.ok(userService.getUserByLoginId(new UserLoginIdRequest(loginId)));
    }

    @GetMapping
    @Override
    public ResponseEntity<UserListResponse> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/duplicate")
    @Override
    public ResponseEntity<Void> checkLoginId(@RequestParam String loginId) {
        userService.checkDuplicatedUserId(new UserLoginIdRequest(loginId));
        return ResponseEntity.ok().build();
    }
}
