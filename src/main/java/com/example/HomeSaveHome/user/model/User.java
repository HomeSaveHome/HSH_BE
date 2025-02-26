package com.example.HomeSaveHome.user.model;

public class User {
    private String username;
    private String email;
    private String password;
    private int point;  // 포인트 필드 추가
    private int level;  // 레벨 필드 추가

    // getters and setters
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

    public int getPoint() {  // 포인트를 가져오는 메서드
        return point;
    }

    public void setPoint(int point) {  // 포인트를 설정하는 메서드
        this.point = point;
    }

    public int getLevel() {  // 레벨을 가져오는 메서드
        return level;
    }

    public void setLevel(int level) {  // 레벨을 설정하는 메서드
        this.level = level;
    }
}
