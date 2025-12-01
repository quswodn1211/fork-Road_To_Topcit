package com.opsw.backend.controller;

import com.opsw.backend.controller.docs.UserSubjectProgressApi;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressGainRequest;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressListQuery;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressQuery;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressResetRequest;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressStartRequest;
import com.opsw.backend.dto.usersubjectprogress.response.UserSubjectProgressListResponse;
import com.opsw.backend.dto.usersubjectprogress.response.UserSubjectProgressResponse;
import com.opsw.backend.service.UserSubjectProgressService;
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
@RequestMapping("/api/user-subject-progresses")
@RequiredArgsConstructor
public class UserSubjectProgressController implements UserSubjectProgressApi {

    private final UserSubjectProgressService userSubjectProgressService;

    @PostMapping("/start")
    @Override
    public ResponseEntity<UserSubjectProgressResponse> startProgress(@RequestBody UserSubjectProgressStartRequest request) {
        return ResponseEntity.ok(userSubjectProgressService.startProgress(request));
    }

    @PostMapping("/gain")
    @Override
    public ResponseEntity<UserSubjectProgressResponse> gainExperience(@RequestBody UserSubjectProgressGainRequest request) {
        return ResponseEntity.ok(userSubjectProgressService.gainExperience(request));
    }

    @PostMapping("/reset")
    @Override
    public ResponseEntity<UserSubjectProgressResponse> resetExperience(@RequestBody UserSubjectProgressResetRequest request) {
        return ResponseEntity.ok(userSubjectProgressService.resetExperience(request));
    }

    @GetMapping
    @Override
    public ResponseEntity<UserSubjectProgressResponse> getProgress(@RequestParam Long userId, @RequestParam Long subjectId) {
        return ResponseEntity.ok(userSubjectProgressService.getProgress(new UserSubjectProgressQuery(userId, subjectId)));
    }

    @GetMapping("/users/{userId}")
    @Override
    public ResponseEntity<UserSubjectProgressListResponse> listProgresses(@PathVariable Long userId) {
        return ResponseEntity.ok(userSubjectProgressService.listProgresses(new UserSubjectProgressListQuery(userId)));
    }
}
