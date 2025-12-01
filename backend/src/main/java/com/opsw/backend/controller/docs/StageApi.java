package com.opsw.backend.controller.docs;

import com.opsw.backend.dto.stage.request.StageAssignStoryRequest;
import com.opsw.backend.dto.stage.request.StageCreateRequest;
import com.opsw.backend.dto.stage.request.StageUpdateRequest;
import com.opsw.backend.dto.stage.response.StageListResponse;
import com.opsw.backend.dto.stage.response.StageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Stages", description = "스테이지 관리 API")
public interface StageApi {

    @Operation(summary = "스테이지 생성", description = "월드에 스테이지를 추가한다. 월드당 최대 3개.")
    ResponseEntity<StageResponse> createStage(StageCreateRequest request);

    @Operation(summary = "스테이지 수정", description = "스테이지 번호를 업데이트한다.")
    ResponseEntity<StageResponse> updateStage(StageUpdateRequest request);

    @Operation(summary = "스토리 배정", description = "스테이지를 특정 스토리에 연결한다.")
    ResponseEntity<StageResponse> assignStory(StageAssignStoryRequest request);

    @Operation(summary = "스테이지 삭제", description = "스테이지 ID로 삭제한다.")
    ResponseEntity<Void> removeStage(Long id);

    @Operation(summary = "스테이지 단건 조회", description = "스테이지 ID로 정보를 조회한다.")
    ResponseEntity<StageResponse> getStage(Long id);

    @Operation(summary = "월드별 스테이지 목록", description = "월드에 속한 스테이지를 번호순으로 조회한다.")
    ResponseEntity<StageListResponse> getStagesByWorld(Long worldId);

    @Operation(summary = "스토리별 스테이지 목록", description = "스토리에 연결된 스테이지 목록을 조회한다.")
    ResponseEntity<StageListResponse> getStagesByStory(Long storyId);
}
