package com.opsw.backend.controller.docs;

import com.opsw.backend.dto.story.request.StoryCreateRequest;
import com.opsw.backend.dto.story.request.StoryUpdateRequest;
import com.opsw.backend.dto.story.response.StoryListResponse;
import com.opsw.backend.dto.story.response.StoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Stories", description = "스토리 관리 API")
public interface StoryApi {

    @Operation(summary = "스토리 생성", description = "월드 ID와 스토리 번호를 지정해 새 스토리를 만든다.")
    ResponseEntity<StoryResponse> createStory(StoryCreateRequest request);

    @Operation(summary = "스토리 번호 수정", description = "스토리 고유 ID로 번호를 변경한다.")
    ResponseEntity<StoryResponse> updateStory(StoryUpdateRequest request);

    @Operation(summary = "스토리 삭제", description = "스토리를 식별자로 삭제한다.")
    ResponseEntity<Void> removeStory(Long id);

    @Operation(summary = "스토리 단건 조회", description = "스토리 ID로 단일 스토리를 조회한다.")
    ResponseEntity<StoryResponse> getStory(Long id);

    @Operation(summary = "월드별 스토리 목록", description = "월드에 속한 모든 스토리를 번호순으로 조회한다.")
    ResponseEntity<StoryListResponse> getStoriesByWorld(Long worldId);
}
