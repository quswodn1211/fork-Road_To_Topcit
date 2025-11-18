package com.opsw.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opsw.backend.domain.Question;
import com.opsw.backend.domain.Subject;
import com.opsw.backend.dto.QuestionCreateRequest;
import com.opsw.backend.dto.QuestionCreateResponse;
import com.opsw.backend.dto.QuestionResponseDto;
import com.opsw.backend.dto.QuestionSimpleDto;
import com.opsw.backend.repository.QuestionRepository;
import com.opsw.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public QuestionCreateResponse createQuestion(QuestionCreateRequest request) {

        // subjectId 기반으로 과목 조회 (필수)
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목 ID입니다."));

        // JSON 변환
        String optionsJson = toJson(request.getOptions());
        String tagsJson = toJson(request.getTags());

        // Question 엔티티 생성
        Question question = Question.builder()
                .content(request.getContent())
                .options(optionsJson)
                .answer(request.getAnswer())
                .qtype(request.getQtype())
                .difficulty(request.getDifficulty())
                .subject(subject)
                .tags(tagsJson)
                .aiPrompt(null)
                .aiOutput(null)
                .build();

        Question saved = questionRepository.save(question);

        return new QuestionCreateResponse(saved.getId(), "문제 생성 완료");
    }

    // 문제 상세 조회
    public QuestionResponseDto getQuestionById(Long id) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제 ID입니다."));

        return QuestionResponseDto.builder()
                .id(question.getId())
                .content(question.getContent())
                .options(jsonToList(question.getOptions()))
                .answer(question.getAnswer())
                .qtype(question.getQtype())
                .difficulty(question.getDifficulty())
                .subjectId(question.getSubject().getId())
                .tags(jsonToList(question.getTags()))
                .build();
    }

    private java.util.List<String> jsonToList(String json) {
        try {
            if (json == null) return java.util.Collections.emptyList();
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    // 과목별 문제 조회 API
    public List<QuestionSimpleDto> getQuestionsBySubject(Long subjectId) {

        List<Question> questions = questionRepository.findBySubject_Id(subjectId,
                org.springframework.data.domain.Pageable.unpaged()).getContent();

        return questions.stream()
                .map(q -> QuestionSimpleDto.builder()
                        .id(q.getId())
                        .content(q.getContent())
                        .difficulty(q.getDifficulty())
                        .build()
                )
                .toList();
    }


    // List → JSON 변환
    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 변환 오류: " + e.getMessage());
        }
    }
}
