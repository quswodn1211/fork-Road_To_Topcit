package com.opsw.backend.dto.world.response;

import com.opsw.backend.domain.Subject;
import com.opsw.backend.domain.World;

public record WorldResponse(
        Long id,
        String name,
        Long subjectId,
        String subjectName
) {

    public static WorldResponse toDto(World world) {
        if (world == null) {
            return null;
        }
        Subject subject = world.getSubject();
        return new WorldResponse(
                world.getId(),
                world.getName(),
                subject != null ? subject.getId() : null,
                subject != null ? subject.getName() : null
        );
    }
}
