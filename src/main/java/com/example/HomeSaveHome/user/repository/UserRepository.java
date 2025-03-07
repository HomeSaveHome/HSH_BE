package com.example.HomeSaveHome.user.repository;

import com.example.HomeSaveHome.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);  // 이메일로 사용자 조회
    User findByUsername(String username);  // 사용자 이름으로 조회
    Optional<User> findById(Long id);  // ID로 사용자 조회
}
