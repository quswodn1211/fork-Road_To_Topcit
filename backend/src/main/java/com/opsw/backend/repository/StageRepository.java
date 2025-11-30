package com.opsw.backend.repository;

import com.opsw.backend.domain.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StageRepository extends JpaRepository<Stage, Long> {

    // 월드 기준으로 스테이지 전체 조회
    List<Stage> findByWorldIdOrderByStageNumberAsc(Long worldId);

    // 스토리 내 스테이지 조회
    List<Stage> findByStoryIdOrderByStageNumberAsc(Long storyId);

    // 월드 + 스테이지 번호 조합으로 단건 조회
    Optional<Stage> findByWorldIdAndStageNumber(Long worldId, int stageNumber);
}
