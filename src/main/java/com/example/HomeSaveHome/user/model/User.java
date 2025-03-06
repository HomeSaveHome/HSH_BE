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
    private Long id;  // 자동 생성되는 ID 추가

    @Column(nullable = false, length = 100, unique = true)  // 유니크한 사용자명
    private String username;

    @Column(nullable = false, length = 255, unique = true)  // 유니크한 이메일
    private String email;

    @Column(nullable = false, length = 255)  // 비밀번호는 nullable(false)로 필수값으로 설정
    private String password;

    @Column(nullable = false)  // 포인트는 필수값으로 설정
    private int point;  // 포인트 필드 추가

    @Column(nullable = false)  // 레벨은 필수값으로 설정
    private int level;  // 레벨 필드 추가

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
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
