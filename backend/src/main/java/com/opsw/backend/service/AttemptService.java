package com.opsw.backend.service;

import com.opsw.backend.domain.Question;
import com.opsw.backend.domain.user.Attempt;
import com.opsw.backend.dto.AttemptHistoryResponse;
import com.opsw.backend.dto.AttemptSubmitRequest;
import com.opsw.backend.dto.AttemptSubmitResponse;
import com.opsw.backend.dto.AttemptResponse;
import com.opsw.backend.repository.AttemptRepository;
import com.opsw.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;

    // ============================
    // 1) 풀이 제출
    // ============================
    public AttemptSubmitResponse submitAttempt(AttemptSubmitRequest request) {

        // 문제 조회
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        // 정답 체크
        boolean isCorrect = question.getAnswer().equals(request.getSubmittedAnswer());

        // XP 계산
        int gainedXp = isCorrect ? 10 : 0;

        // 저장
        Attempt attempt = Attempt.builder()
                .userId(request.getUserId())
                .questionId(request.getQuestionId())
                .submittedAnswer(request.getSubmittedAnswer())
                .isCorrect(isCorrect)
                .gainedXp(gainedXp)
                .build();

        attemptRepository.save(attempt);

        // 응답 변환
        return new AttemptSubmitResponse(
                attempt.getId(),
                isCorrect,
                question.getAnswer(),
                gainedXp
        );
    }

    // ============================
    // 2) 문제별 풀이 이력 조회
    // ============================
    public List<AttemptResponse> getAttemptsByQuestionId(Long questionId) {

        List<Attempt> attempts =
                attemptRepository.findByQuestionIdOrderByCreatedAtDesc(questionId);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return attempts.stream()
                .map(a -> AttemptResponse.builder()
                        .attemptId(a.getId())
                        .userId(a.getUserId())
                        .submittedAnswer(a.getSubmittedAnswer())
                        .isCorrect(a.isCorrect())
                        .gainedXp(a.getGainedXp())
                        .createdAt(a.getCreatedAt().format(fmt))
                        .build()
                )
                .toList();
    }

    public List<AttemptResponse> getAttemptsByUserId(Long userId) {

        List<Attempt> attempts = attemptRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return attempts.stream()
                .map(a -> new AttemptResponse(
                        a.getId(),
                        a.getUserId(),
                        a.getQuestionId(),
                        a.getSubmittedAnswer(),
                        a.isCorrect(),
                        a.getGainedXp(),
                        a.getCreatedAt() == null ? null : a.getCreatedAt().toString()
                ))
                .toList();
    }
}
