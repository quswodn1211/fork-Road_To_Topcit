package com.opsw.backend.repository;

import com.opsw.backend.domain.World;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorldRepository extends JpaRepository<World, Long> {

    Optional<World> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    Optional<World> findBySubjectId(Long subjectId);

    boolean existsBySubjectId(Long subjectId);

    List<World> findAllByOrderByIdAsc();
}
