package com.opsw.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 월드내 진행 단위 (총 3개, 각 5문제)
// stageNumber는 1부터 시작함
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stage",
        indexes = @Index(name = "idx_stage_world", columnList = "world_id"),
        uniqueConstraints = @UniqueConstraint(name = "uk_stage_world_number",
                columnNames = {"world_id", "stage_number"}))
public class Stage {

    public static final int PROBLEM_PER_STAGE = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "world_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private World world;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Story story;

    @Column(name = "stage_number", nullable = false)
    private int stageNumber;

    @Column(name = "problem_count", nullable = false)
    private int problemCount = PROBLEM_PER_STAGE;

    private Stage(World world, int stageNumber) {
        if (world == null) {
            throw new IllegalArgumentException("월드는 필수입니다");
        }
        this.world = world;
        changeStageNumber(stageNumber);
        this.problemCount = PROBLEM_PER_STAGE;
    }

    public static Stage of(World world, int stageNumber) {
        return new Stage(world, stageNumber);
    }

    public void changeStageNumber(int stageNumber) {
        validateStageNumber(stageNumber);
        this.stageNumber = stageNumber;
    }

    public void relocate(World world) {
        if (world == null) {
            throw new IllegalArgumentException("월드는 필수입니다");
        }
        this.world = world;
    }

    public void assignStory(Story story) {
        this.story = story;
    }

    private void validateStageNumber(int stageNumber) {
        if (stageNumber < 1 || stageNumber > World.MAX_STAGE_COUNT) {
            throw new IllegalArgumentException(
                    "스테이지 번호는 1부터 " + World.MAX_STAGE_COUNT + "사이여야 합니다");
        }
    }
}
