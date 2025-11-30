package com.opsw.backend.controller.docs;

import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressGainRequest;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressListQuery;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressQuery;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressResetRequest;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressStartRequest;
import com.opsw.backend.dto.usersubjectprogress.response.UserSubjectProgressListResponse;
import com.opsw.backend.dto.usersubjectprogress.response.UserSubjectProgressResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User Subject Progress", description = "사용자 과목 경험치 API")
public interface UserSubjectProgressApi {

    @Operation(summary = "경험치 추적 시작", description = "사용자와 과목 조합에 대한 경험치 추적을 시작한다.")
    ResponseEntity<UserSubjectProgressResponse> startProgress(UserSubjectProgressStartRequest request);

    @Operation(summary = "경험치 상승", description = "지정된 경험치를 더해 과목별 경험치를 기록한다.")
    ResponseEntity<UserSubjectProgressResponse> gainExperience(UserSubjectProgressGainRequest request);

    @Operation(summary = "경험치 초기화", description = "특정 사용자/과목 조합의 경험치를 0으로 리셋한다.")
    ResponseEntity<UserSubjectProgressResponse> resetExperience(UserSubjectProgressResetRequest request);

    @Operation(summary = "경험치 조회", description = "사용자와 과목 ID로 단일 진행도를 조회한다.")
    ResponseEntity<UserSubjectProgressResponse> getProgress(Long userId, Long subjectId);

    @Operation(summary = "사용자별 경험치 목록", description = "사용자의 모든 과목 경험치를 조회한다.")
    ResponseEntity<UserSubjectProgressListResponse> listProgresses(Long userId);
}
