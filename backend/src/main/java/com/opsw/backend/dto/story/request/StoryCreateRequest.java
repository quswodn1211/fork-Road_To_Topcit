package com.opsw.backend.dto.story.request;

public record StoryCreateRequest(
        Long worldId,
        int storyNumber
) {
}
