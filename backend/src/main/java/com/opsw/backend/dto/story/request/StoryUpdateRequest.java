package com.opsw.backend.dto.story.request;

public record StoryUpdateRequest(
        Long storyId,
        int storyNumber
) {
}
