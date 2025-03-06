package com.example.HomeSaveHome.user.service;

import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원가입 처리 메서드
    public boolean registerUser(User user) {
        // 사용자명 중복 체크
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false;  // 이미 존재하는 사용자 이름
        }

        // 이메일 중복 체크
        existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return false;  // 이미 존재하는 이메일
        }

        userRepository.save(user);  // 새로운 사용자 저장
        return true;
    }

    // 로그인 인증 처리 메서드
    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
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
        return null;  // 사용자 존재하지 않으면 null 반환
    }

    // 사용자 삭제
    public boolean deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());  // ID로 사용자 삭제
            return true;
        }
        return false;  // 사용자가 존재하지 않으면 삭제 실패
    }

    // ID로 사용자 조회
    public User getUserById(Long userId) {
        // Optional을 사용하여 사용자 존재 여부 확인
        return userRepository.findById(userId).orElse(null);  // ID로 사용자 조회
    }
}
