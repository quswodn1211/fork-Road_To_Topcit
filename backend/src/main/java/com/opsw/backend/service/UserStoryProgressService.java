package com.opsw.backend.service;

import com.opsw.backend.domain.Stage;
import com.opsw.backend.domain.World;
import com.opsw.backend.domain.user.User;
import com.opsw.backend.domain.user.UserStoryProgress;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressAdvanceRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressListQuery;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressLoseLifeRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressQuery;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressRefillRequest;
import com.opsw.backend.dto.userstoryprogress.request.UserStoryProgressStartRequest;
import com.opsw.backend.dto.userstoryprogress.response.UserStoryProgressListResponse;
import com.opsw.backend.dto.userstoryprogress.response.UserStoryProgressResponse;
import com.opsw.backend.repository.StageRepository;
import com.opsw.backend.repository.UserRepository;
import com.opsw.backend.repository.UserStoryProgressRepository;
import com.opsw.backend.repository.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStoryProgressService {

    private final UserStoryProgressRepository userStoryProgressRepository;
    private final UserRepository userRepository;
    private final WorldRepository worldRepository;
    private final StageRepository stageRepository;

    @Transactional
    public UserStoryProgressResponse startProgress(UserStoryProgressStartRequest request) {
        UserStoryProgress progress = userStoryProgressRepository
                .findByUserIdAndWorldId(request.userId(), request.worldId())
                .orElseGet(() -> {
                    User user = loadUser(request.userId());
                    World world = loadWorld(request.worldId());
                    if (request.stageLife() > 0) {
                        return userStoryProgressRepository.save(
                                UserStoryProgress.start(user, world, request.stageLife()));
                    }
                    return userStoryProgressRepository.save(UserStoryProgress.start(user, world));
                });
        return UserStoryProgressResponse.toDto(progress);
    }

    @Transactional
    public UserStoryProgressResponse advanceStage(UserStoryProgressAdvanceRequest request) {
        UserStoryProgress progress = loadProgress(request.userId(), request.worldId());
        Stage stage = loadStage(request.stageId());
        if (!stage.getWorld().getId().equals(progress.getWorld().getId())) {
            throw new IllegalArgumentException("Stage does not belong to the target world");
        }
        progress.advance(stage);
        return UserStoryProgressResponse.toDto(progress);
    }

    @Transactional
    public UserStoryProgressResponse loseLife(UserStoryProgressLoseLifeRequest request) {
        UserStoryProgress progress = loadProgress(request.userId(), request.worldId());
        progress.loseLife();
        return UserStoryProgressResponse.toDto(progress);
    }

    @Transactional
    public UserStoryProgressResponse refillLife(UserStoryProgressRefillRequest request) {
        UserStoryProgress progress = loadProgress(request.userId(), request.worldId());
        progress.refillLife(request.stageLife());
        return UserStoryProgressResponse.toDto(progress);
    }

    public UserStoryProgressResponse getProgress(UserStoryProgressQuery request) {
        return UserStoryProgressResponse.toDto(loadProgress(request.userId(), request.worldId()));
    }

    public UserStoryProgressListResponse listProgresses(UserStoryProgressListQuery request) {
        return UserStoryProgressListResponse.toDto(
                userStoryProgressRepository.findByUserIdOrderByWorldIdAsc(request.userId())
        );
    }

    private UserStoryProgress loadProgress(Long userId, Long worldId) {
        return userStoryProgressRepository.findByUserIdAndWorldId(userId, worldId)
                .orElseThrow(() -> new IllegalArgumentException("Progress not found"));
    }

    private Stage loadStage(Long stageId) {
        return stageRepository.findById(stageId)
                .orElseThrow(() -> new IllegalArgumentException("Stage not found"));
    }

    private User loadUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private World loadWorld(Long id) {
        return worldRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("World not found"));
    }
}
