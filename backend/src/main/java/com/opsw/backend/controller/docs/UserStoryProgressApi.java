package com.opsw.backend.controller.docs;

import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressAdvanceRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressListQuery;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressLoseLifeRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressQuery;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressRefillRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressStartRequest;
import com.opsw.backend.dto.userstoryprogress.response.UserStoryProgressListResponse;
import com.opsw.backend.dto.userstoryprogress.response.UserStoryProgressResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User Story Progress", description = "스토리 모드 진행 API")
public interface UserStoryProgressApi {

    @Operation(summary = "진행 시작", description = "사용자와 월드 조합에 대한 스토리 진행을 초기화한다.")
    ResponseEntity<UserStoryProgressResponse> startProgress(UserStoryProgressStartRequest request);

    @Operation(summary = "스테이지 갱신", description = "현재 진행 중인 스테이지를 갱신한다.")
    ResponseEntity<UserStoryProgressResponse> advanceStage(UserStoryProgressAdvanceRequest request);

    @Operation(summary = "목숨 감소", description = "사용자의 스테이지 목숨을 1 감소시킨다.")
    ResponseEntity<UserStoryProgressResponse> loseLife(UserStoryProgressLoseLifeRequest request);

    @Operation(summary = "목숨 보충", description = "사용자의 스테이지 목숨을 지정된 값으로 회복한다.")
    ResponseEntity<UserStoryProgressResponse> refillLife(UserStoryProgressRefillRequest request);

    @Operation(summary = "진행 조회", description = "사용자와 월드 조합으로 진행 상태를 조회한다.")
    ResponseEntity<UserStoryProgressResponse> getProgress(Long userId, Long worldId);

    @Operation(summary = "사용자별 진행 목록", description = "사용자가 진행 중인 모든 월드의 상태를 조회한다.")
    ResponseEntity<UserStoryProgressListResponse> listProgresses(Long userId);
}
