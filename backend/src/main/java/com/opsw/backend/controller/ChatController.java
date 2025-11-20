package com.opsw.backend.controller;

import com.opsw.backend.dto.ChatRequest;
import com.opsw.backend.dto.ChatResponse;
import com.opsw.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatService.ask(request);
    }
}
