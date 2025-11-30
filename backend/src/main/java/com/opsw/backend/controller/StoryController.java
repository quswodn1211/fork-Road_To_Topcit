package com.opsw.backend.controller;

import com.opsw.backend.controller.docs.StoryApi;
import com.opsw.backend.dto.story.request.StoryCreateRequest;
import com.opsw.backend.dto.story.request.StoryIdRequest;
import com.opsw.backend.dto.story.request.StoryRemoveRequest;
import com.opsw.backend.dto.story.request.StoryUpdateRequest;
import com.opsw.backend.dto.story.request.StoryWorldQuery;
import com.opsw.backend.dto.story.response.StoryListResponse;
import com.opsw.backend.dto.story.response.StoryResponse;
import com.opsw.backend.service.StoryService;
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
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController implements StoryApi {

    private final StoryService storyService;

    @PostMapping
    @Override
    public ResponseEntity<StoryResponse> createStory(@RequestBody StoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.createStory(request));
    }

    @PatchMapping
    @Override
    public ResponseEntity<StoryResponse> updateStory(@RequestBody StoryUpdateRequest request) {
        return ResponseEntity.ok(storyService.updateStoryNumber(request));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> removeStory(@PathVariable Long id) {
        storyService.removeStory(new StoryRemoveRequest(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<StoryResponse> getStory(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.getStory(new StoryIdRequest(id)));
    }

    @GetMapping("/world/{worldId}")
    @Override
    public ResponseEntity<StoryListResponse> getStoriesByWorld(@PathVariable Long worldId) {
        return ResponseEntity.ok(storyService.getStoriesByWorld(new StoryWorldQuery(worldId)));
    }
}
