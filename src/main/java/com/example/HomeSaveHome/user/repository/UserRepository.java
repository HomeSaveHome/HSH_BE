package com.example.HomeSaveHome.user.repository;

import com.example.HomeSaveHome.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자 조회
    User findByEmail(String email);

    // 사용자 이름으로 사용자 조회
    Optional<User> findByUsername(String username);

    // 사용자 ID로 사용자 조회
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 이메일을 통해 사용자 인증을 위한 존재 여부 확인
    Optional<User> findByEmailAndPassword(String email, String password);
}