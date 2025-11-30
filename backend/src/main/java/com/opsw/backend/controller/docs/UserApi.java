package com.opsw.backend.controller.docs;

import com.opsw.backend.dto.user.request.UserChangePasswordRequest;
import com.opsw.backend.dto.user.request.UserRegisterRequest;
import com.opsw.backend.dto.user.response.UserListResponse;
import com.opsw.backend.dto.user.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Users", description = "회원 계정 API")
public interface UserApi {

    @Operation(summary = "회원 가입", description = "로그인 아이디와 비밀번호로 새 계정을 생성한다.")
    ResponseEntity<UserResponse> register(UserRegisterRequest request);

    @Operation(summary = "비밀번호 변경", description = "로그인 아이디와 기존 비밀번호를 검증한 뒤 새 비밀번호로 교체한다.")
    ResponseEntity<UserResponse> changePassword(UserChangePasswordRequest request);

    @Operation(summary = "회원 삭제", description = "기본 키(ID)로 회원 계정을 삭제한다.")
    ResponseEntity<Void> remove(Long id);

    @Operation(summary = "회원 단건 조회", description = "기본 키(ID)로 회원 정보를 조회한다.")
    ResponseEntity<UserResponse> getUser(Long id);

    @Operation(summary = "로그인 ID로 조회", description = "로그인 아이디를 이용해 회원 정보를 조회한다.")
    ResponseEntity<UserResponse> getByLoginId(String loginId);

    @Operation(summary = "회원 목록", description = "가입된 모든 회원을 조회한다.")
    ResponseEntity<UserListResponse> listUsers();

    @Operation(summary = "로그인 ID 중복 검사", description = "로그인 아이디 사용 가능 여부를 확인한다.")
    ResponseEntity<Void> checkLoginId(String loginId);
}
