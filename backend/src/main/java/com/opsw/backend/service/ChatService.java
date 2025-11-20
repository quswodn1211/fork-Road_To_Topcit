package com.opsw.backend.service;

import com.opsw.backend.dto.ChatRequest;
import com.opsw.backend.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RestTemplate restTemplate;

    /**
     * ai.enabled = false → AI 서버 없이 로컬 Mock 응답 모드
     * ai.enabled = true  → AI 서버로 실제 요청 모드
     */
    @Value("${ai.enabled:false}")
    private boolean aiEnabled;

    /**
     * AI 서버의 엔드포인트
     * 예: http://localhost:5007/api/ai/chat
     */
    @Value("${ai.url:http://localhost:5007/api/ai/chat}")
    private String aiChatUrl;


    /** ============================
     *     /api/chat 핵심 로직
     *  ============================ */
    public ChatResponse ask(ChatRequest request) {

        String userMessage = request.getMessage();

        log.info("ChatService.ask() received message: {}", userMessage);

        // 1) AI 서버 OFF → Mock 응답 반환
        if (!aiEnabled) {
            log.info("AI disabled → using local mock response.");
            return localChat(userMessage);
        }

        // 2) AI 서버 ON → AI 서버에 전달
        try {
            log.info("Sending request to AI Server: {}", aiChatUrl);

            ChatResponse aiResponse = restTemplate.postForObject(
                    aiChatUrl,
                    request,
                    ChatResponse.class
            );

            log.info("AI Server Response: {}", aiResponse);

            if (aiResponse == null) {
                throw new RuntimeException("AI 서버 응답 NULL");
            }

            return aiResponse;

        } catch (Exception e) {
            log.error("AI Server Error: {}", e.getMessage());
            // AI 서버 오류 시 fallback
            return fallbackResponse(userMessage);
        }
    }


    /** ============
     *  로컬 Mock 응답
     *  (AI 미개발 + 테스트용)
     *  ============ */
    private ChatResponse localChat(String msg) {

        String answer;

        if (msg.contains("정규화")) {
            answer = "정규화는 데이터 중복을 줄이고 테이블을 효율적으로 구성하는 과정이에요!";
        } else if (msg.contains("인덱스")) {
            answer = "인덱스는 검색을 빠르게 해주는 자료구조입니다.";
        } else if (msg.contains("JOIN")) {
            answer = "JOIN은 여러 테이블을 연결해 한 번에 조회하는 SQL 연산입니다.";
        } else {
            answer = "AI 서버가 준비되지 않아 로컬에서 임시 답변을 제공합니다.\n"
                    + "질문: " + msg + "\n"
                    + "답변: AI 서버가 붙으면 더욱 정확한 답변을 제공할 수 있어요!";
        }

        return new ChatResponse(answer);
    }


    /** ============
     *  AI 서버 오류용 fallback
     *  ============ */
    private ChatResponse fallbackResponse(String userMessage) {

        String answer =
                "AI 서버에 연결할 수 없어 로컬 임시 답변을 제공합니다.\n" +
                        "\n[사용자 질문] " + userMessage +
                        "\n[임시 답변] 정규화는 데이터 품질을 높이기 위한 DB 설계 기법입니다!";

        return new ChatResponse(answer);
    }
}
