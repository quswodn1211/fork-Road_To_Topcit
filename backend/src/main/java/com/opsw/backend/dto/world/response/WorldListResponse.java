package com.opsw.backend.dto.world.response;

import com.opsw.backend.domain.World;

import java.util.List;
import java.util.stream.Collectors;

public record WorldListResponse(
        List<WorldResponse> worlds
) {

    public static WorldListResponse toDto(List<World> worlds) {
        List<WorldResponse> items = worlds == null
                ? List.of()
                : worlds.stream()
                .map(WorldResponse::toDto)
                .collect(Collectors.toList());
        return new WorldListResponse(items);
    }
}
