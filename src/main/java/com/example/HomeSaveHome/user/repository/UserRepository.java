package com.example.HomeSaveHome.user.repository;

import com.example.HomeSaveHome.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;  // Optional을 import 합니다.

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findById(Long id);  // id로 찾는 메서드 추가
}
