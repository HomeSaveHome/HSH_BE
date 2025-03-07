package com.example.HomeSaveHome.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/users/signup", "/users/login").permitAll()
                .anyRequest().permitAll() // Allows all requests without authentication
                .and()
                .formLogin().disable() // Disables Spring Security login form
                .logout().disable(); // Disables logout handling

        return http.build();
    }
}
