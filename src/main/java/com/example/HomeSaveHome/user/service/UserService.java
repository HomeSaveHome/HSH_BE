package com.example.HomeSaveHome.user.service;

import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원가입 처리 메서드
    public boolean registerUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false;  // 이미 존재하는 사용자 이름
        }
        userRepository.save(user);  // 새로운 사용자 저장
        return true;
    }

    // 로그인 인증 처리 메서드
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;  // 인증 성공
        }
        return false;  // 인증 실패
    }

    // 현재 사용자 반환 (예시로 항상 첫 번째 사용자 반환)
    public User getCurrentUser() {
        return userRepository.findByUsername("testuser");  // 예시로 'testuser' 사용
    }

    // 프로필 수정
    public User updateUserProfile(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setPoint(user.getPoint());
            existingUser.setLevel(user.getLevel());
            return userRepository.save(existingUser);  // 업데이트된 사용자 저장
        }
        return null;
    }

    // 사용자 삭제
    public boolean deleteUser(Long userId) {
        userRepository.deleteById(userId);  // ID로 사용자 삭제
        return true;
    }

    // ID로 사용자 조회
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);  // ID로 사용자 조회
    }
}
