package com.example.HomeSaveHome.user.controller;

import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignUpForm() {
        return "users/signup";  // Point this to your login page
    }

    // POST 방식 : 회원가입 처리
    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, Model model) {
        boolean isSuccess = userService.registerUser(user);
        if (isSuccess) {
            model.addAttribute("message", "회원가입 성공!");
        } else {
            model.addAttribute("message", "회원가입 실패! 이미 존재하는 사용자 이름 또는 이메일입니다.");
        }
        return "users/result";  // 결과 페이지
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";  // Point this to your login page
    }

    // POST 방식 : 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        boolean isSuccess = userService.authenticateUser(email, password);
        System.out.println("로그인 정보: " + email + ", " + password);

        if (isSuccess) {
            // User user = userService.getUserById(username);  // Fetch user details by username
            // return "redirect:/users/info/" + user.getId();  // Redirect to user info page
            return "mainpage/main2";  // 임시 Redirect to the main page
        } else {
            model.addAttribute("errorMessage", "로그인 실패! 사용자명이나 비밀번호를 확인해주세요.");
            return "users/login";  // Return the login page with an error message
        }
    }


    // GET 방식 : ID로 사용자 조회
    @GetMapping("info/{userId}")
    public String getUserById(@PathVariable("userId") Long userId, Model model) {
        User user = userService.getUserById(userId);
        if (user != null) {
            model.addAttribute("user", user);
            return "users/userDetail"; // 사용자 상세 정보 페이지
        } else {
            model.addAttribute("message", "사용자를 찾을 수 없습니다.");
            return "users/result";
        }
    }

    // 프로필 수정
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute User user, Model model) {
        User updatedUser = userService.updateUserProfile(user);
        if (updatedUser != null) {
            model.addAttribute("message", "프로필이 성공적으로 업데이트되었습니다.");
            model.addAttribute("user", updatedUser);
        } else {
            model.addAttribute("message", "프로필 업데이트 실패! 사용자 정보를 확인해주세요.");
        }
        return "users/result"; // 결과 페이지
    }

    // 사용자 삭제
    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId, Model model) {
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) {
            model.addAttribute("message", "사용자가 삭제되었습니다.");
        } else {
            model.addAttribute("message", "사용자를 찾을 수 없거나 삭제 실패.");
        }
        return "users/result"; // 결과 페이지
    }

    // 기타 메서드들 (프로필 수정, 삭제 등)
}
