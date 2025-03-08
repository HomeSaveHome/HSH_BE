package com.example.HomeSaveHome.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.HomeSaveHome.user.repository.UserRepository;
import com.example.HomeSaveHome.user.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("로그인 시도 중" + email);
        User user = userRepository.findByEmail(email);
        if(user == null) {
            System.out.println("안됨");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
