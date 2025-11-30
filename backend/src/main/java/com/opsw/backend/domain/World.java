package com.opsw.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// 각 과목을 대표하는 월드
// 하나의 월드는 여러 스테이지를 가질수 있다
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "world",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_world_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_world_subject", columnNames = "subject_id")
        })
public class World {

    public static final int MAX_STAGE_COUNT = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), unique = true)
    private Subject subject;

    @OneToMany(mappedBy = "world", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Stage> stages = new ArrayList<>();

    private World(String name, Subject subject) {
        if (name == null) {
            throw new IllegalArgumentException("월드 이름은 필수입니다");
        }
        if (subject == null) {
            throw new IllegalArgumentException("과목은 필수입니다");
        }
        this.name = name;
        this.subject = subject;
    }

    public static World of(String name, Subject subject) {
        return new World(name, subject);
    }

    public void rename(String name) {
        if (name == null) {
            throw new IllegalArgumentException("월드 이름은 필수입니다");
        }
        this.name = name;
    }

    public void assignSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("과목은 필수입니다");
        }
        this.subject = subject;
    }

    public void addStage(Stage stage) {
        if (stage == null || stages.size() >= MAX_STAGE_COUNT) {
            return;
        }
        stages.add(stage);
        stage.relocate(this);
    }

    public List<Stage> getStages() {
        return stages;
    }
}
