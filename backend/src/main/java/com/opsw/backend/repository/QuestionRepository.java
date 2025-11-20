package com.opsw.backend.repository;

import com.opsw.backend.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 과목 ID로 문제 페이지 조회
    Page<Question> findBySubject_Id(Long subjectId, Pageable pageable);

    // 문제 유형(qtype: MCQ/SHORT) + 난이도(difficulty: Easy/Normal/Hard/Expert)로 필터
    Page<Question> findByQtypeIgnoreCaseAndDifficultyIgnoreCase(String qtype, String difficulty, Pageable pageable);

    // 키워드(본문/태그 JSON 문자열) 대략 검색
    @Query("""
           SELECT q FROM Question q
           WHERE LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(COALESCE(q.tags, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<Question> searchByKeyword(String keyword, Pageable pageable);

    List<Question> findBySubject_Id(Long subjectId);

}
