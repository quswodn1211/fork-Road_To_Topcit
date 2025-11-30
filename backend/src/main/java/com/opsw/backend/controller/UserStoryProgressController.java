package com.opsw.backend.controller;

import com.opsw.backend.controller.docs.UserStoryProgressApi;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressAdvanceRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressListQuery;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressLoseLifeRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressQuery;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressRefillRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressStartRequest;
import com.opsw.backend.dto.userstoryprogress.response.UserStoryProgressListResponse;
import com.opsw.backend.dto.userstoryprogress.response.UserStoryProgressResponse;
import com.opsw.backend.service.UserStoryProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-story-progresses")
@RequiredArgsConstructor
public class UserStoryProgressController implements UserStoryProgressApi {

    private final UserStoryProgressService userStoryProgressService;

    @PostMapping("/start")
    @Override
    public ResponseEntity<UserStoryProgressResponse> startProgress(@RequestBody UserStoryProgressStartRequest request) {
        return ResponseEntity.ok(userStoryProgressService.startProgress(request));
    }

    @PostMapping("/advance")
    @Override
    public ResponseEntity<UserStoryProgressResponse> advanceStage(@RequestBody UserStoryProgressAdvanceRequest request) {
        return ResponseEntity.ok(userStoryProgressService.advanceStage(request));
    }

    @PostMapping("/lose-life")
    @Override
    public ResponseEntity<UserStoryProgressResponse> loseLife(@RequestBody UserStoryProgressLoseLifeRequest request) {
        return ResponseEntity.ok(userStoryProgressService.loseLife(request));
    }

    @PostMapping("/refill-life")
    @Override
    public ResponseEntity<UserStoryProgressResponse> refillLife(@RequestBody UserStoryProgressRefillRequest request) {
        return ResponseEntity.ok(userStoryProgressService.refillLife(request));
    }

    @GetMapping
    @Override
    public ResponseEntity<UserStoryProgressResponse> getProgress(@RequestParam Long userId, @RequestParam Long worldId) {
        return ResponseEntity.ok(userStoryProgressService.getProgress(new UserStoryProgressQuery(userId, worldId)));
    }

    @GetMapping("/users/{userId}")
    @Override
    public ResponseEntity<UserStoryProgressListResponse> listProgresses(@PathVariable Long userId) {
        return ResponseEntity.ok(userStoryProgressService.listProgresses(new UserStoryProgressListQuery(userId)));
    }
}
