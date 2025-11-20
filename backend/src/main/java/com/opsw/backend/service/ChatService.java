package com.opsw.backend.service;

import com.opsw.backend.dto.ChatRequest;
import com.opsw.backend.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;

    @Value("${ai.enabled:false}")
    private boolean aiEnabled;

    @Value("${ai.url:http://localhost:5007}")
    private String aiUrl;


    /** ============================
     *     /api/chat 기능 핵심 로직
     *  ============================ */
    public ChatResponse ask(ChatRequest request) {

        String userMessage = request.getMessage();

        // 1) AI 비활성화 → 로컬 응답
        if (!aiEnabled) {
            return localChat(userMessage);
        }

        // 2) AI 사용 O → 실제 AI 서버로 전달
        try {
            return restTemplate.postForObject(
                    aiUrl + "/chat",
                    request,
                    ChatResponse.class
            );
        } catch (Exception e) {
            // AI 서버 장애 시 fallback
            return new ChatResponse(
                    "AI 서버 오류로 인해 로컬 응답을 제공합니다.\n" +
                            "[사용자 질문] " + userMessage + "\n" +
                            "[로컬 답변] 정규화는 데이터 중복을 제거하고 구조를 체계화하는 과정입니다!"
            );
        }
    }


    /** ============
     *  로컬 응답
     *  (AI 없이 테스트 가능)
     *  ============ */
    private ChatResponse localChat(String msg) {

        String answer;

        if (msg.contains("정규화")) {
            answer = "정규화는 데이터 중복을 줄이고 테이블 구조를 체계적으로 만드는 과정이에요!";
        } else if (msg.contains("인덱스")) {
            answer = "인덱스는 책의 목차처럼 데이터를 빠르게 찾도록 도와주는 구조입니다.";
        } else if (msg.contains("JOIN")) {
            answer = "JOIN은 여러 테이블을 연결하여 원하는 데이터를 한 번에 조회하는 SQL 연산입니다.";
        } else {
            answer = "아직 AI 서버가 없어 로컬에서 임시 답변을 제공하고 있어요.\n"
                    + "질문: " + msg + "\n"
                    + "답변: 나중에 AI가 붙으면 더 똑똑하게 대답해줄게요! 😊";
        }

        return new ChatResponse(answer);
    }

}
