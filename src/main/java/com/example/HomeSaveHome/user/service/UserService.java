package com.example.HomeSaveHome.user.service;
// 실제 DB에 저장된 로직을 여기에 작성

import com.example.HomeSaveHome.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // 회원가입 처리 메서드
    public boolean registerUser(User user) {
        System.out.println("회원가입 정보: " + user.getUsername() + ", " + user.getEmail());
        return true;
    }

    // 로그인 인증 처리 메서드
    public boolean authenticateUser(String username, String password) {
        // 아직 DB 연동 안해서 ID : testuser와 비밀번호 password123만 제공
        return "testuser".equals(username) && "password123".equals(password);
    }
}

