package com.opsw.backend.controller.docs;

import com.opsw.backend.dto.world.request.WorldCreateRequest;
import com.opsw.backend.dto.world.request.WorldUpdateRequest;
import com.opsw.backend.dto.world.response.WorldListResponse;
import com.opsw.backend.dto.world.response.WorldResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Worlds", description = "과목별 월드 관리 API")
public interface WorldApi {

    @Operation(summary = "월드 생성", description = "과목을 지정해 새로운 월드를 생성한다.")
    ResponseEntity<WorldResponse> createWorld(WorldCreateRequest request);

    @Operation(summary = "월드 수정", description = "월드 ID로 이름 및 연결 과목을 변경한다.")
    ResponseEntity<WorldResponse> updateWorld(WorldUpdateRequest request);

    @Operation(summary = "월드 삭제", description = "월드를 ID 기준으로 삭제한다.")
    ResponseEntity<Void> deleteWorld(Long id);

    @Operation(summary = "월드 단건 조회", description = "월드 ID로 월드를 조회한다.")
    ResponseEntity<WorldResponse> getWorld(Long id);

    @Operation(summary = "이름으로 월드 조회", description = "월드 이름으로 단건 월드를 조회한다.")
    ResponseEntity<WorldResponse> getWorldByName(String name);

    @Operation(summary = "월드 목록", description = "생성 순서대로 모든 월드를 조회한다.")
    ResponseEntity<WorldListResponse> listWorlds();

    @Operation(summary = "월드 이름 중복 검사", description = "월드 이름 사용 가능 여부를 확인한다.")
    ResponseEntity<Void> checkWorldName(String name);
}
