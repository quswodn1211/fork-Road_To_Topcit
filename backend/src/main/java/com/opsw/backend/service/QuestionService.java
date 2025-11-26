package com.opsw.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opsw.backend.domain.Question;
import com.opsw.backend.domain.Subject;
import com.opsw.backend.dto.*;
import com.opsw.backend.repository.QuestionRepository;
import com.opsw.backend.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ai.enabled:false}")
    private boolean aiEnabled;

    @Value("${ai.url:http://localhost:5007}")
    private String aiUrl;


    /* ===============================
     * 0) JSON 변환 보조 메소드
     * =============================== */
    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("JSON 변환 오류");
        }
    }

    private List<String> jsonToList(String json) {
        try {
            if (json == null) return Collections.emptyList();
            return objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    /* ====================================
     * 1) 기존 문제 생성 API
     * ==================================== */
    public QuestionCreateResponse createQuestion(QuestionCreateRequest request) {

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목 ID입니다."));

        String optionsJson = toJson(request.getOptions());
        String tagsJson = toJson(request.getTags());

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


    /* ====================================
     * 2) 기존 문제 상세 조회 API
     * ==================================== */
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


    /* ====================================
     * 3) 기존 과목별 문제 조회 API
     * ==================================== */
    public List<QuestionSimpleDto> getQuestionsBySubject(Long subjectId) {

        List<Question> questions = questionRepository.findBySubject_Id(
                subjectId, org.springframework.data.domain.Pageable.unpaged()
        ).getContent();

        return questions.stream()
                .map(q -> QuestionSimpleDto.builder()
                        .id(q.getId())
                        .content(q.getContent())
                        .difficulty(q.getDifficulty())
                        .build()
                )
                .toList();
    }


    /* ====================================
     * 4) AI 기반 문제 생성 API
     * ==================================== */
    public QuestionAiGenerateResponse generateQuestionByAI(QuestionAiGenerateRequest req) {

        Subject subject = subjectRepository.findById(req.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목 ID입니다."));

        AiQuestionDto aiResult = aiEnabled
                ? callAiServer(req)
                : generateDummyQuestion(req);

        String optionsJson = toJson(aiResult.getOptions());

        Question question = Question.builder()
                .content(aiResult.getContent())
                .options(optionsJson)
                .answer(aiResult.getAnswer())
                .qtype("MCQ")
                .difficulty(req.getDifficulty())
                .subject(subject)
                .build();

        Question saved = questionRepository.save(question);

        return QuestionAiGenerateResponse.builder()
                .id(saved.getId())
                .content(aiResult.getContent())
                .options(aiResult.getOptions())
                .answer(aiResult.getAnswer())
                .difficulty(req.getDifficulty())
                .build();
    }


    /* ====================================
     * 5) AI 서버 호출
     * ==================================== */
    private AiQuestionDto callAiServer(QuestionAiGenerateRequest req) {

        RestTemplate rest = new RestTemplate();

        var body = new java.util.HashMap<String, Object>();
        body.put("subjectId", req.getSubjectId());
        body.put("difficulty", req.getDifficulty());

        var response = rest.postForObject(
                aiUrl + "/generate-question",
                body,
                java.util.Map.class
        );

        return new AiQuestionDto(
                (String) response.get("content"),
                (List<String>) response.get("options"),
                (String) response.get("answer")
        );
    }

    /* ====================================
     * 6) AI 미구현 → 더미 문제 생성
     * ==================================== */
    private AiQuestionDto generateDummyQuestion(QuestionAiGenerateRequest req) {

        return new AiQuestionDto(
                "다음 중 제3정규형(3NF)에 대한 설명으로 옳은 것은?",
                List.of(
                        "부분적 함수 종속을 허용한다",
                        "이행적 함수 종속이 존재하지 않는다",
                        "모든 속성이 기본키에 종속된다",
                        "정규화의 마지막 단계이다"
                ),
                "이행적 함수 종속이 존재하지 않는다"
        );
    }
}
