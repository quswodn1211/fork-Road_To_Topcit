package com.opsw.backend.dto.story.response;

import com.opsw.backend.domain.Story;

public record StoryResponse(
        Long id,
        Long worldId,
        int storyNumber
) {

    public static StoryResponse toDto(Story story) {
        if (story == null) {
            return null;
        }
        Long worldId = story.getWorld() != null ? story.getWorld().getId() : null;
        return new StoryResponse(story.getId(), worldId, story.getStoryNum());
    }
}
