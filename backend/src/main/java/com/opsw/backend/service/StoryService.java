package com.opsw.backend.service;

import com.opsw.backend.domain.Story;
import com.opsw.backend.domain.World;
import com.opsw.backend.dto.story.request.StoryCreateRequest;
import com.opsw.backend.dto.story.request.StoryIdRequest;
import com.opsw.backend.dto.story.request.StoryRemoveRequest;
import com.opsw.backend.dto.story.request.StoryUpdateRequest;
import com.opsw.backend.dto.story.request.StoryWorldQuery;
import com.opsw.backend.dto.story.response.StoryListResponse;
import com.opsw.backend.dto.story.response.StoryResponse;
import com.opsw.backend.repository.StoryRepository;
import com.opsw.backend.repository.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;
    private final WorldRepository worldRepository;

    @Transactional
    public StoryResponse createStory(StoryCreateRequest request) {
        World world = loadWorld(request.worldId());
        ensureUniqueStoryNumber(world.getId(), request.storyNumber(), null);
        Story story = Story.of(world, request.storyNumber());
        storyRepository.save(story);
        return StoryResponse.toDto(story);
    }

    @Transactional
    public StoryResponse updateStoryNumber(StoryUpdateRequest request) {
        Story story = loadStory(request.storyId());
        if (story.getStoryNum() != request.storyNumber()) {
            ensureUniqueStoryNumber(story.getWorld().getId(), request.storyNumber(), story.getId());
            story.changeStoryNum(request.storyNumber());
        }
        return StoryResponse.toDto(story);
    }

    @Transactional
    public void removeStory(StoryRemoveRequest request) {
        Story story = loadStory(request.storyId());
        storyRepository.delete(story);
    }

    public StoryResponse getStory(StoryIdRequest request) {
        return StoryResponse.toDto(loadStory(request.storyId()));
    }

    public StoryListResponse getStoriesByWorld(StoryWorldQuery request) {
        return StoryListResponse.toDto(
                storyRepository.findByWorldIdOrderByStoryNumAsc(request.worldId())
        );
    }

    private void ensureUniqueStoryNumber(Long worldId, int storyNumber, Long storyId) {
        storyRepository.findByWorldIdAndStoryNum(worldId, storyNumber)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(storyId)) {
                        throw new IllegalArgumentException("이미 존재하는 스토리 번호입니다");
                    }
                });
    }

    private Story loadStory(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스토리를 찾을수 없습니다"));
    }

    private World loadWorld(Long id) {
        return worldRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("월드를 찾을수 없습니다"));
    }
}
