package com.opsw.backend.dto.userstoryprogress.response;

import com.opsw.backend.domain.Stage;
import com.opsw.backend.domain.World;
import com.opsw.backend.domain.user.User;
import com.opsw.backend.domain.user.UserStoryProgress;

public record UserStoryProgressResponse(
        Long id,
        Long userId,
        Long worldId,
        Long stageId,
        int stageLife
) {

    public static UserStoryProgressResponse toDto(UserStoryProgress progress) {
        if (progress == null) {
            return null;
        }
        User user = progress.getUser();
        World world = progress.getWorld();
        Stage stage = progress.getStage();
        return new UserStoryProgressResponse(
                progress.getId(),
                user != null ? user.getId() : null,
                world != null ? world.getId() : null,
                stage != null ? stage.getId() : null,
                progress.getStageLife()
        );
    }
}
