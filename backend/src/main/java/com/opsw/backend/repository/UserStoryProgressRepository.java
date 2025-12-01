package com.opsw.backend.repository;

import com.opsw.backend.domain.user.UserStoryProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStoryProgressRepository extends JpaRepository<UserStoryProgress, Long> {

    // 사용자별 월드 진행 현황 조회
    List<UserStoryProgress> findByUserIdOrderByWorldIdAsc(Long userId);

    // 사용자 + 월드 조합으로 단건 조회
    Optional<UserStoryProgress> findByUserIdAndWorldId(Long userId, Long worldId);
}
