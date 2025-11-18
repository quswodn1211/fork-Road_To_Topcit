package com.opsw.backend.controller;

import com.opsw.backend.dto.QuestionCreateRequest;
import com.opsw.backend.dto.QuestionCreateResponse;
import com.opsw.backend.dto.QuestionResponseDto;
import com.opsw.backend.dto.QuestionSimpleDto;
import com.opsw.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    // 문제 생성 API
    @PostMapping
    public QuestionCreateResponse createQuestion(@RequestBody QuestionCreateRequest request) {
        return questionService.createQuestion(request);
    }

    //  문제 상세 조회 API
    @GetMapping("/{id}")
    public QuestionResponseDto getQuestion(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    // 과목별 문제 조회 API
    @GetMapping
    public List<QuestionSimpleDto> getQuestionsBySubject(
            @RequestParam(value = "subject", required = false) Long subjectId
    ) {
        if (subjectId == null) {
            throw new IllegalArgumentException("subject 파라미터가 필요합니다.");
        }
        return questionService.getQuestionsBySubject(subjectId);
    }

}
