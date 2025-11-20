package com.opsw.backend.repository;

import com.opsw.backend.domain.user.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    // 특정 문제(questionId)의 풀이 이력을 모두 조회
    List<Attempt> findByQuestionIdOrderByCreatedAtDesc(Long questionId);
}
