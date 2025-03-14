package com.example.HomeSaveHome.user.service;

import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입 처리 메서드 (비밀번호 암호화 제거, 평문 그대로 저장)
    public boolean registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 로그 찍기 (이게 중요)
        System.out.println("가입 시도하는 유저 정보: " + user);

        userRepository.save(user);
        return true;
    }

    // 기존 로그인 인증 (평문 비밀번호 비교)
    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            System.out.println("DB에 저장된 비밀번호: " + user.getPassword());
            System.out.println("입력된 비밀번호: " + password);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return true;  // 인증 성공
            }
        }
        return false;  // 인증 실패
    }

    // 새로운 로그인 처리 (SecurityContext 저장까지 포함, 평문 비밀번호 비교)
    public boolean authenticateAndSetContext(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("로그인 성공, SecurityContext 설정됨: " + SecurityContextHolder.getContext().getAuthentication());
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

    // 프로필 수정 (비밀번호 암호화 제거, 평문 비밀번호 그대로 저장)
    public User updateUserProfile(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + user.getUsername()));

        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        } // 평문 비밀번호 그대로 저장
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