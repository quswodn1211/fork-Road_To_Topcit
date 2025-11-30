package com.opsw.backend.repository;

import com.opsw.backend.domain.user.UserSubjectProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSubjectProgressRepository extends JpaRepository<UserSubjectProgress, Long> {

    // 사용자별 과목 진행 상황 전체 조회
    List<UserSubjectProgress> findByUserIdOrderBySubjectNameAsc(Long userId);

    // 사용자 + 과목 조합으로 단건 조회
    Optional<UserSubjectProgress> findByUserIdAndSubjectId(Long userId, Long subjectId);
}
