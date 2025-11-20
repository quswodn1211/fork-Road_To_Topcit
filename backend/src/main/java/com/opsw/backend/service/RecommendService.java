package com.opsw.backend.service;

import com.opsw.backend.domain.Question;
import com.opsw.backend.domain.user.Attempt;
import com.opsw.backend.dto.TodayRecommendResponse;
import com.opsw.backend.dto.WeaknessResponse;
import com.opsw.backend.repository.AttemptRepository;
import com.opsw.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final AttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.enabled:false}")
    private boolean aiEnabled;

    @Value("${ai.url:http://localhost:5007}")
    private String aiUrl;


    /** ======================
     *   1) 약점 분석 API
     *  ====================== */
    public WeaknessResponse getWeakness(Long userId) {

        List<Attempt> attempts = attemptRepository.findByUserId(userId);

        // ★ AI 사용 X → 로컬 더미 분석
        if (!aiEnabled) {
            return localWeakness(attempts);
        }

        // ★ AI 사용 O → AI 서버 호출
        try {
            List<Map<String, Object>> payload = buildWeaknessPayload(attempts);

            return restTemplate.postForObject(
                    aiUrl + "/analyze/weakness",
                    payload,
                    WeaknessResponse.class
            );

        } catch (Exception e) {
            return new WeaknessResponse(
                    "AI 분석 불가",
                    0,
                    "AI 서버 연결 오류",
                    "잠시 후 다시 시도해주세요."
            );
        }
    }


    /** 더미 약점 분석 (AI 없을 때 로컬 계산) */
    private WeaknessResponse localWeakness(List<Attempt> attempts) {

        if (attempts.isEmpty()) {
            return new WeaknessResponse(
                    "데이터 없음",
                    0,
                    "아직 풀이한 문제가 없습니다.",
                    "자유모드에서 문제를 먼저 풀어보세요."
            );
        }

        // Attempt → Question 매핑 후 SubjectName으로 그룹화
        Map<String, List<Attempt>> groupBySubject = new HashMap<>();

        for (Attempt a : attempts) {
            Question q = questionRepository.findById(a.getQuestionId()).orElse(null);
            if (q == null) continue;

            String subjectName = q.getSubject().getName();

            groupBySubject.computeIfAbsent(subjectName, key -> new ArrayList<>())
                    .add(a);
        }

        // 정답률 계산
        String weakestSubject = null;
        double lowestAccuracy = 200;

        for (String subjectName : groupBySubject.keySet()) {

            List<Attempt> list = groupBySubject.get(subjectName);

            double accuracy = (double) list.stream()
                    .filter(Attempt::isCorrect)
                    .count() / list.size() * 100;

            if (accuracy < lowestAccuracy) {
                lowestAccuracy = accuracy;
                weakestSubject = subjectName;
            }
        }

        return new WeaknessResponse(
                weakestSubject,
                (int) lowestAccuracy,
                weakestSubject + " 과목의 정답률이 가장 낮습니다.",
                weakestSubject + " 핵심 개념부터 복습해보세요!"
        );
    }



    private List<Map<String, Object>> buildWeaknessPayload(List<Attempt> attempts) {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Attempt a : attempts) {
            Question q = questionRepository.findById(a.getQuestionId()).orElse(null);
            if (q == null) continue;

            Map<String, Object> m = new HashMap<>();
            m.put("questionId", q.getId());
            m.put("subjectId", q.getSubject().getId());
            m.put("subjectName", q.getSubject().getName());
            m.put("correct", a.isCorrect());
            m.put("difficulty", q.getDifficulty());
            m.put("content", q.getContent());
            list.add(m);
        }
        return list;
    }


    /** ===========================
     *   2) 오늘의 학습 추천 API
     *  =========================== */
    public TodayRecommendResponse getTodayRecommend(Long userId) {

        // 최근 풀이 데이터
        List<Attempt> attempts = attemptRepository.findByUserId(userId);

        // AI off → 로컬 랜덤 추천
        if (!aiEnabled) {
            return localTodayRecommend();
        }

        // AI on → 실제 AI 호출
        try {
            return restTemplate.getForObject(
                    aiUrl + "/recommend/today?userId=" + userId,
                    TodayRecommendResponse.class
            );
        } catch (Exception e) {
            return localTodayRecommend(); // fallback
        }
    }


    /** 로컬 랜덤 추천 (AI 없이) */
    private TodayRecommendResponse localTodayRecommend() {

        List<Question> all = questionRepository.findAll();
        Collections.shuffle(all);

        List<TodayRecommendResponse.ProblemItem> problems = all.stream()
                .limit(2)
                .map(q -> new TodayRecommendResponse.ProblemItem(
                        q.getId(),
                        q.getSubject().getId(),
                        q.getDifficulty()
                ))
                .toList();

        return new TodayRecommendResponse(
                problems,
                "랜덤 추천 (AI 비활성)"
        );
    }
}
