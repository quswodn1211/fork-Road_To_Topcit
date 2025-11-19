package com.opsw.backend.controller;

import com.opsw.backend.dto.AttemptHistoryResponse;
import com.opsw.backend.dto.AttemptSubmitRequest;
import com.opsw.backend.dto.AttemptSubmitResponse;
import com.opsw.backend.dto.AttemptResponse;
import com.opsw.backend.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    // ============================
    // 1) 풀이 제출
    // ============================
    @PostMapping
    public ResponseEntity<AttemptSubmitResponse> submitAttempt(
            @RequestBody AttemptSubmitRequest request) {

        return ResponseEntity.ok(attemptService.submitAttempt(request));
    }

    // ============================
    // 2) 문제별 풀이 이력 조회
    // ============================
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AttemptResponse>> getAttemptsByQuestion(
            @PathVariable Long questionId) {

        return ResponseEntity.ok(attemptService.getAttemptsByQuestionId(questionId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<AttemptResponse>> getMyAttempts(@RequestParam Long userId) {

        List<AttemptResponse> response = attemptService.getAttemptsByUserId(userId);
        return ResponseEntity.ok(response);
    }



}
