package com.example.HomeSaveHome.user.model;

import jakarta.persistence.*;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username"}),
                @UniqueConstraint(columnNames = {"email"})
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID 자동 생성
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private int point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @PrePersist  // 엔티티가 저장되기 전에 기본 값 설정
    public void prePersist() {
        if (this.level == null) {
            this.level = Level.UNRANKED;  // 기본 값 UNRANKED 설정
        }
    }

    // 포인트 증가 시 레벨 업데이트
    public void increasePoint(int amount) {
        this.point += amount;
        updateLevel();  // 포인트가 증가할 때마다 레벨을 업데이트
    }

    // 레벨을 업데이트하는 메소드
    private void updateLevel() {
        if (point == 0) {
            level = Level.UNRANKED;  // 0점일 때는 UNRANKED
        } else if (point >= 1 && point <= 100) {
            level = Level.BRONZE;  // 1점 이상 100점 미만은 BRONZE
        } else if (point >= 600) {
            level = Level.CHALLENGER;  // 600점 이상은 CHALLENGER
        } else if (point >= 500) {
            level = Level.DIAMOND;
        } else if (point >= 400) {
            level = Level.PLATINUM;
        } else if (point >= 300) {
            level = Level.GOLD;
        } else if (point >= 200) {
            level = Level.SILVER;
        }
    }

    // 레벨을 문자열에서 Enum으로 변환하여 설정
    public void setLevel(String levelStr) {
        try {
            this.level = Level.valueOf(levelStr.toUpperCase());  // 문자열을 Enum 값으로 변환
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 레벨 값입니다: " + levelStr);  // 예외 처리
        }
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
        updateLevel();  // 포인트가 변경될 때마다 레벨을 갱신
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    // Enum 정의
    public enum Level {
        UNRANKED, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND, CHALLENGER
    }
}
