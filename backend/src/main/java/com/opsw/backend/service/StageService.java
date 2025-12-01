package com.opsw.backend.service;

import com.opsw.backend.domain.Stage;
import com.opsw.backend.domain.Story;
import com.opsw.backend.domain.World;
import com.opsw.backend.dto.stage.request.StageAssignStoryRequest;
import com.opsw.backend.dto.stage.request.StageCreateRequest;
import com.opsw.backend.dto.stage.request.StageIdRequest;
import com.opsw.backend.dto.stage.request.StageRemoveRequest;
import com.opsw.backend.dto.stage.request.StageStoryQuery;
import com.opsw.backend.dto.stage.request.StageUpdateRequest;
import com.opsw.backend.dto.stage.request.StageWorldQuery;
import com.opsw.backend.dto.stage.response.StageListResponse;
import com.opsw.backend.dto.stage.response.StageResponse;
import com.opsw.backend.repository.StageRepository;
import com.opsw.backend.repository.StoryRepository;
import com.opsw.backend.repository.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageService {

    private final StageRepository stageRepository;
    private final WorldRepository worldRepository;
    private final StoryRepository storyRepository;

    @Transactional
    public StageResponse createStage(StageCreateRequest request) {
        World world = loadWorld(request.worldId());
        List<Stage> stages = stageRepository.findByWorldIdOrderByStageNumberAsc(world.getId());
        if (stages.size() >= World.MAX_STAGE_COUNT) {
            throw new IllegalArgumentException("월드가 이미 최대 스테이지 개수를 가지고 있습니다");
        }
        ensureStageNumberAvailable(world.getId(), request.stageNumber(), null);
        Stage stage = Stage.of(world, request.stageNumber());
        stageRepository.save(stage);
        return StageResponse.toDto(stage);
    }

    @Transactional
    public StageResponse updateStage(StageUpdateRequest request) {
        Stage stage = loadStage(request.stageId());
        if (stage.getStageNumber() != request.stageNumber()) {
            ensureStageNumberAvailable(stage.getWorld().getId(), request.stageNumber(), stage.getId());
            stage.changeStageNumber(request.stageNumber());
        }
        return StageResponse.toDto(stage);
    }

    @Transactional
    public StageResponse assignStory(StageAssignStoryRequest request) {
        Stage stage = loadStage(request.stageId());
        Story story = loadStory(request.storyId());
        if (!story.getWorld().getId().equals(stage.getWorld().getId())) {
            throw new IllegalArgumentException("스토리와 스테이지는 같은 월드에 속해야 합니다");
        }
        stage.assignStory(story);
        return StageResponse.toDto(stage);
    }

    @Transactional
    public void removeStage(StageRemoveRequest request) {
        Stage stage = loadStage(request.stageId());
        stageRepository.delete(stage);
    }

    public StageResponse getStage(StageIdRequest request) {
        return StageResponse.toDto(loadStage(request.stageId()));
    }

    public StageListResponse getStagesByWorld(StageWorldQuery request) {
        return StageListResponse.toDto(
                stageRepository.findByWorldIdOrderByStageNumberAsc(request.worldId())
        );
    }

    public StageListResponse getStagesByStory(StageStoryQuery request) {
        return StageListResponse.toDto(
                stageRepository.findByStoryIdOrderByStageNumberAsc(request.storyId())
        );
    }

    private Stage loadStage(Long id) {
        return stageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스테이지를 찾을수 없습니다"));
    }

    private World loadWorld(Long id) {
        return worldRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("월드를 찾을수 없습니다"));
    }

    private Story loadStory(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스토리를 찾을수 없습니다"));
    }

    private void ensureStageNumberAvailable(Long worldId, int stageNumber, Long stageId) {
        stageRepository.findByWorldIdAndStageNumber(worldId, stageNumber)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(stageId)) {
                        throw new IllegalArgumentException("이미 존재하는 스테이지 번호입니다");
                    }
                });
    }
}
