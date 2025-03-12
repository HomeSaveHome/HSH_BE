package com.example.HomeSaveHome.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class UserSecurityConfig {

    // 🔹 PasswordEncoder (비밀번호 암호화 설정)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔹 AuthenticationManager 설정 (BCrypt 적용)
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder); // BCrypt 설정

        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화 (필요에 따라 활성화 가능)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션이 필요한 경우에만 생성
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/users/signup", "/users/login", "/users/register").permitAll()  // 로그인 및 회원가입 페이지 접근 허용
                        .requestMatchers("/mainpage/**").hasRole("USER")  // mainpage는 USER 권한 필요
                        .anyRequest().authenticated()  // 그 외의 요청은 인증 필요
                )
                .formLogin(login -> login
                        .loginPage("/users/login")  // 로그인 페이지 설정
                        .usernameParameter("email")
                        .loginProcessingUrl("/users/login")  // 로그인 폼 처리 URL
                        .defaultSuccessUrl("/main", true)  // 로그인 성공 시 이동할 URL
                        .failureUrl("/users/login?error=invalid_credentials")  // 로그인 실패 시 이동할 URL
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")  // 로그아웃 URL 설정
                        .logoutSuccessUrl("/")  // 로그아웃 성공 후 이동할 URL
                        .permitAll()
                );

        return http.build();
    }
}
