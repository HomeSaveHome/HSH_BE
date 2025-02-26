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

    // 로그아웃 로직
    public boolean logoutUser() {
        System.out.println("사용자 로그아웃 처리!");
        return true;
    }

    // UserService.java 내부에 현재 사용자 반환 메서드 (기존 예시 코드 수정)
    public User getCurrentUser() {
        // 실제 구현 시, SecurityContext 또는 세션을 통해 현재 로그인 사용자를 가져옵니다.
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPoint(1000); // 예시 포인트
        user.setLevel(5);    // 예시 레벨
        return user;
    }


    public User updateUserProfile(User user) {
        // 실제 DB 업데이트 로직 구현. 여기선 단순 로그 출력 예시.
        System.out.println("프로필 업데이트: " + user.getUsername() + ", " + user.getEmail());
        // 업데이트가 성공했다고 가정하고 user 객체를 반환.
        return user;
    }

    public boolean deleteUser() {
        // 실제 구현 시, 현재 로그인된 사용자 삭제 로직 구현
        System.out.println("사용자 삭제 처리");
        return true;
    }

    public User getUserById(String userid) {
        // 실제 구현 시, DB에서 userid를 이용하여 사용자 정보를 조회합니다.
        // 예시로, testuser인 경우 반환, 아닐 경우 null 반환
        if ("testuser".equals(userid)) {
            User user = new User();
            user.setUsername("testuser");
            user.setEmail("testuser@example.com");
            return user;
        }
        return null;
    }

}

