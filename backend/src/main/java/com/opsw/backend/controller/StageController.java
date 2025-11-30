package com.opsw.backend.controller;

import com.opsw.backend.controller.docs.StageApi;
import com.opsw.backend.dto.stage.request.StageAssignStoryRequest;
import com.opsw.backend.dto.stage.request.StageCreateRequest;
import com.opsw.backend.dto.stage.request.StageIdRequest;
import com.opsw.backend.dto.stage.request.StageRemoveRequest;
import com.opsw.backend.dto.stage.request.StageStoryQuery;
import com.opsw.backend.dto.stage.request.StageUpdateRequest;
import com.opsw.backend.dto.stage.request.StageWorldQuery;
import com.opsw.backend.dto.stage.response.StageListResponse;
import com.opsw.backend.dto.stage.response.StageResponse;
import com.opsw.backend.service.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stages")
@RequiredArgsConstructor
public class StageController implements StageApi {

    private final StageService stageService;

    @PostMapping
    @Override
    public ResponseEntity<StageResponse> createStage(@RequestBody StageCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stageService.createStage(request));
    }

    @PatchMapping
    @Override
    public ResponseEntity<StageResponse> updateStage(@RequestBody StageUpdateRequest request) {
        return ResponseEntity.ok(stageService.updateStage(request));
    }

    @PostMapping("/assign-story")
    @Override
    public ResponseEntity<StageResponse> assignStory(@RequestBody StageAssignStoryRequest request) {
        return ResponseEntity.ok(stageService.assignStory(request));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> removeStage(@PathVariable Long id) {
        stageService.removeStage(new StageRemoveRequest(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<StageResponse> getStage(@PathVariable Long id) {
        return ResponseEntity.ok(stageService.getStage(new StageIdRequest(id)));
    }

    @GetMapping("/world/{worldId}")
    @Override
    public ResponseEntity<StageListResponse> getStagesByWorld(@PathVariable Long worldId) {
        return ResponseEntity.ok(stageService.getStagesByWorld(new StageWorldQuery(worldId)));
    }

    @GetMapping("/story/{storyId}")
    @Override
    public ResponseEntity<StageListResponse> getStagesByStory(@PathVariable Long storyId) {
        return ResponseEntity.ok(stageService.getStagesByStory(new StageStoryQuery(storyId)));
    }
}
