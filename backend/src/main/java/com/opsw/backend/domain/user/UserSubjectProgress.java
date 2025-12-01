package com.opsw.backend.domain.user;

import com.opsw.backend.domain.Subject;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자별 과목 경험치를 추적한다
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_subject_progress",
        uniqueConstraints = @UniqueConstraint(name = "uk_progress_user_subject",
                columnNames = {"user_id", "subject_id"}))
public class UserSubjectProgress {

    public static final int MAX_EXPERIENCE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Subject subject;

    @Column(nullable = false)
    private int experience;

    private UserSubjectProgress(User user, Subject subject) {
        this.user = user;
        this.subject = subject;
        this.experience = 0;
    }

    public static UserSubjectProgress start(User user, Subject subject) {
        return new UserSubjectProgress(user, subject);
    }

    public void gain(int amount) {
        if (amount <= 0) {
            return;
        }
        int next = this.experience + amount;
        this.experience = Math.min(next, MAX_EXPERIENCE);
    }

    public void reset() {
        this.experience = 0;
    }
}
