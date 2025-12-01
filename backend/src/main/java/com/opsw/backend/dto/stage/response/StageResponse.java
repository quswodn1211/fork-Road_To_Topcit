package com.opsw.backend.dto.stage.response;

import com.opsw.backend.domain.Stage;
import com.opsw.backend.domain.Story;
import com.opsw.backend.domain.World;

public record StageResponse(
        Long id,
        Long worldId,
        Long storyId,
        int stageNumber,
        int problemCount
) {

    public static StageResponse toDto(Stage stage) {
        if (stage == null) {
            return null;
        }
        World world = stage.getWorld();
        Story story = stage.getStory();
        return new StageResponse(
                stage.getId(),
                world != null ? world.getId() : null,
                story != null ? story.getId() : null,
                stage.getStageNumber(),
                stage.getProblemCount()
        );
    }
}
