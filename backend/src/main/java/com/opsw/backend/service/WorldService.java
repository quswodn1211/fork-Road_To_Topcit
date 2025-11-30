package com.opsw.backend.service;

import com.opsw.backend.domain.Subject;
import com.opsw.backend.domain.World;
import com.opsw.backend.dto.world.request.*;
import com.opsw.backend.dto.world.request.WorldIdRequest;
import com.opsw.backend.dto.world.response.*;
import com.opsw.backend.repository.SubjectRepository;
import com.opsw.backend.repository.WorldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorldService {

    private final WorldRepository worldRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public WorldResponse createWorld(WorldCreateRequest request) {
        validateName(request.name());
        Subject subject = loadSubject(request.subjectId());
        ensureNameAvailable(request.name(), null);
        ensureSubjectAvailable(subject.getId(), null);
        World world = World.of(request.name(), subject);
        worldRepository.save(world);
        return WorldResponse.toDto(world);
    }

    @Transactional
    public WorldResponse updateWorld(WorldUpdateRequest request) {
        World world = loadWorld(request.worldId());
        if (request.name() != null && !request.name().trim().isEmpty()
                && !world.getName().equalsIgnoreCase(request.name())) {
            ensureNameAvailable(request.name(), world.getId());
            world.rename(request.name());
        }
        if (request.subjectId() != null
                && !request.subjectId().equals(world.getSubject().getId())) {
            Subject subject = loadSubject(request.subjectId());
            ensureSubjectAvailable(subject.getId(), world.getId());
            world.assignSubject(subject);
        }
        return WorldResponse.toDto(world);
    }

    @Transactional
    public void removeWorld(WorldIdRequest request) {
        World world = loadWorld(request.worldId());
        worldRepository.delete(world);
    }

    public WorldResponse getWorld(WorldIdRequest request) {
        return WorldResponse.toDto(loadWorld(request.worldId()));
    }

    public WorldResponse getWorldByName(WorldNameRequest request) {
        validateName(request.name());
        World world = worldRepository.findByNameIgnoreCase(request.name())
                .orElseThrow(() -> new IllegalArgumentException("월드를 찾을수 없습니다"));
        return WorldResponse.toDto(world);
    }

    public WorldListResponse listWorlds() {
        return WorldListResponse.toDto(worldRepository.findAllByOrderByIdAsc());
    }

    public void checkDuplicatedName(WorldNameRequest request) {
        validateName(request.name());
        if (worldRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 월드 이름입니다");
        }
    }

    private void ensureNameAvailable(String name, Long worldId) {
        worldRepository.findByNameIgnoreCase(name).ifPresent(existing -> {
            if (!existing.getId().equals(worldId)) {
                throw new IllegalArgumentException("이미 존재하는 월드 이름입니다");
            }
        });
    }

    private void ensureSubjectAvailable(Long subjectId, Long worldId) {
        worldRepository.findBySubjectId(subjectId).ifPresent(existing -> {
            if (!existing.getId().equals(worldId)) {
                throw new IllegalArgumentException("과목이 이미 다른 월드에 할당되어있습니다");
            }
        });
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("월드 이름은 필수입니다");
        }
    }

    private World loadWorld(Long id) {
        return worldRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("월드를 찾을수 없습니다"));
    }

    private Subject loadSubject(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("과목을 찾을수 없습니다"));
    }
}
