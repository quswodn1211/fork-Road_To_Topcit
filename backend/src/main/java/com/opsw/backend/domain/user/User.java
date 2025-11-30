package com.opsw.backend.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 계정 정보
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "app_user",
        indexes = {
                @Index(name = "idx_user_user_id", columnList = "user_id", unique = true)
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 50, nullable = false, unique = true)
    private String loginId;

    @Column(length = 255, nullable = false)
    private String password;

    private User(String loginId, String password) {
        if (loginId == null) {
            throw new IllegalArgumentException("로그인 아이디는 필수입니다");
        }
        if (password == null) {
            throw new IllegalArgumentException("비밀번호는 필수입니다");
        }
        this.loginId = loginId;
        this.password = password;
    }

    public static User create(String loginId, String password) {
        return new User(loginId, password);
    }

    public void changePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("비밀번호는 필수입니다");
        }
        this.password = password;
    }
}
