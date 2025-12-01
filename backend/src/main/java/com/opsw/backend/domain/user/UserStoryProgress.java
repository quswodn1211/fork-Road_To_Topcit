package com.opsw.backend.domain.user;

import com.opsw.backend.domain.Stage;
import com.opsw.backend.domain.World;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자별 스토리 진행 현황과 스테이즈 목숨 상태를 관리한다
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_story_progress",
        uniqueConstraints = @UniqueConstraint(name = "uk_progress_user_world",
                columnNames = {"user_id", "world_id"}))
public class UserStoryProgress {

    public static final int DEFAULT_STAGE_LIFE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "world_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private World world;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Stage stage;

    @Column(name = "stage_life", nullable = false)
    private int stageLife;

    private UserStoryProgress(User user, World world, int initialLife) {
        if (user == null) {
            throw new IllegalArgumentException("사용자는 필수입니다");
        }
        if (world == null) {
            throw new IllegalArgumentException("월드는 필수입니다");
        }
        this.user = user;
        this.world = world;
        this.stageLife = normalizeLife(initialLife);
    }

    public static UserStoryProgress start(User user, World world) {
        return new UserStoryProgress(user, world, DEFAULT_STAGE_LIFE);
    }

    public static UserStoryProgress start(User user, World world, int initialLife) {
        return new UserStoryProgress(user, world, initialLife);
    }

    public void advance(Stage stage) {
        this.stage = stage;
        resetStageLife();
    }

    public void loseLife() {
        if (stageLife > 0) {
            stageLife--;
        }
    }

    public boolean needsRestart() {
        return stageLife <= 0;
    }

    public void resetStageLife() {
        this.stageLife = DEFAULT_STAGE_LIFE;
    }

    public void refillLife(int life) {
        this.stageLife = normalizeLife(life);
    }

    private int normalizeLife(int life) {
        if (life <= 0) {
            return 0;
        }
        return Math.min(life, DEFAULT_STAGE_LIFE);
    }
}
