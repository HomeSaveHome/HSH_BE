package com.example.HomeSaveHome.user.service;

import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 회원가입 처리 메서드
    public boolean registerUser(User user) {
        // 사용자명 중복 체크
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }

        // 이메일 중복 체크
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        // 비밀번호 암호화 후 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    // 기존 로그인 인증 (단순 체크용)
    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;  // 인증 성공
        }
        return false;  // 인증 실패
    }

    // 새로운 로그인 처리 (SecurityContext 저장까지 포함)
    public boolean authenticateAndSetContext(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 로그인 성공 시 SecurityContext에 인증정보 저장
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            return true;
        }
        return false;
    }

    // 현재 로그인된 사용자 반환 (SecurityContext 활용)
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    // 프로필 수정
    public User updateUserProfile(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + user.getUsername()));

        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setPoint(user.getPoint());
        existingUser.setLevel(user.getLevel());
        return userRepository.save(existingUser);
    }

    // 사용자 삭제
    public boolean deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    // ID로 사용자 조회
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
