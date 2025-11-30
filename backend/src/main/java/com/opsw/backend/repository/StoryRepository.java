package com.opsw.backend.repository;

import com.opsw.backend.domain.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Long> {

    // 특정 월드의 스토리를 번호순으로 조회
    List<Story> findByWorldIdOrderByStoryNumAsc(Long worldId);

    // 월드 + 스토리 번호 조합으로 단건 조회
    Optional<Story> findByWorldIdAndStoryNum(Long worldId, int storyNum);
}
