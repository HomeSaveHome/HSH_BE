//package com.example.HomeSaveHome.user.security;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()  // CSRF 비활성화 (개발 환경에서만 사용)
//                .authorizeRequests()
//                .requestMatchers("/users/signup", "/users/login").permitAll()  // 회원가입 및 로그인은 누구나 접근 가능
//                .anyRequest().authenticated()  // 나머지 요청은 인증된 사용자만 접근 가능
//                .and()
//                .formLogin()
//                .loginPage("/users/login")  // 커스텀 로그인 페이지
//                .permitAll()  // 로그인 페이지는 누구나 접근 가능
//                .successHandler(new CustomAuthenticationSuccessHandler())  // 로그인 성공 핸들러 추가
//                .failureHandler(new CustomAuthenticationFailureHandler())  // 로그인 실패 핸들러 추가
//                .and()
//                .logout()
//                .permitAll();  // 로그아웃 페이지는 누구나 접근 가능
//        return http.build();  // Spring Security 5 이후 방식
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();  // 패스워드 인코딩 설정
//    }
//
//    // 로그인 성공 핸들러
//    private class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//        @Override
//        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                            Authentication authentication) throws IOException, ServletException {
//            System.out.println("로그인 성공! 사용자: " + authentication.getName());
//            response.sendRedirect("/home");  // 로그인 성공 후 홈 페이지로 리디렉션
//        }
//    }
//
//    // 로그인 실패 핸들러
//    private class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//        @Override
//        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                                            AuthenticationException exception) throws IOException, ServletException {
//
//            System.out.println("로그인 실패! 오류: " + exception.getMessage());
//            response.sendRedirect("/users/login?error=true");  // 로그인 실패 시 로그인 페이지로 리디렉션
//        }
//    }
//}
