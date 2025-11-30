package com.opsw.backend.dto.stage.response;

import com.opsw.backend.domain.Stage;

import java.util.List;
import java.util.stream.Collectors;

public record StageListResponse(
        List<StageResponse> stages
) {

    public static StageListResponse toDto(List<Stage> stages) {
        List<StageResponse> items = stages == null
                ? List.of()
                : stages.stream()
                .map(StageResponse::toDto)
                .collect(Collectors.toList());
        return new StageListResponse(items);
    }
}
