package com.opsw.backend.controller;

import com.opsw.backend.dto.TodayRecommendResponse;
import com.opsw.backend.dto.WeaknessResponse;
import com.opsw.backend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/weakness")
    public WeaknessResponse getWeakness(@RequestParam Long userId) {
        return recommendService.getWeakness(userId);
    }

    @GetMapping("/today")
    public TodayRecommendResponse getToday(@RequestParam Long userId) {
        return recommendService.getTodayRecommend(userId);
    }
}


