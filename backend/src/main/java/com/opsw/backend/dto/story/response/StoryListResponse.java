package com.opsw.backend.dto.story.response;

import com.opsw.backend.domain.Story;

import java.util.List;
import java.util.stream.Collectors;

public record StoryListResponse(
        List<StoryResponse> stories
) {

    public static StoryListResponse toDto(List<Story> stories) {
        List<StoryResponse> items = stories == null
                ? List.of()
                : stories.stream()
                .map(StoryResponse::toDto)
                .collect(Collectors.toList());
        return new StoryListResponse(items);
    }
}
