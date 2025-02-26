package com.example.HomeSaveHome.user.controller;

import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller  // @RestController -> @Controller로 변경
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // POST 방식 : 회원가입 처리
    @PostMapping("/signup")
    public String singUp(@ModelAttribute User user, Model model) {
        boolean isSuccess = userService.registerUser(user);
        if (isSuccess) {
            model.addAttribute("message", "회원가입 성공!");
        } else {
            model.addAttribute("message", "회원가입 실패!");
        }
        // 회원가입 결과 페이지로 리다이렉트
        return "users/result";  // /src/main/resources/templates/result.html로 렌더링
    }

    // GET 방식 : 회원가입 페이지 제공 (나중에 제거해야할 것)
    @GetMapping("/signup")
    public String getSignupForm() {
        return "users/signup";  // /src/main/resources/templates/signup.html로 렌더링
    }

    // POST 방식 : 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        boolean isSuccess = userService.authenticateUser(username, password);
        if (isSuccess) {
            return "redirect:/users/me";  // Redirect to profile page
        } else {
            model.addAttribute("message", "로그인 실패!");
            return "users/result"; // Show failure message
        }
    }

    // GET 방식 : 로그인 페이지 제공 (나중에 프론트랑 합칠 때 제거할 예정)
    @GetMapping("/login")
    public String getLoginForm() {
        return "users/login";
    }

    @PostMapping("/logout")
    public String logout(Model model) {
        boolean isSuccess = userService.logoutUser();
        if (isSuccess) {
            model.addAttribute("message", "로그아웃 성공!");
        }
        else {
            model.addAttribute("message", "로그아웃 실패!");
        }
        return "users/result"; // 나중에 리액트로 연결해야할 것.
    }

    @GetMapping("/me")
    public String getProfile(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        return "users/profile"; // 프로필 페이지 (profile.html) 렌더링
    }

    @PutMapping("/me")
    public String updateProfile(@ModelAttribute User user, Model model) {
        User updatedUser = userService.updateUserProfile(user);
        if (updatedUser != null) {
            model.addAttribute("user", updatedUser);
            model.addAttribute("message", "프로필 수정 성공!");
        } else {
            model.addAttribute("message", "프로필 수정 실패!");
        }
        return "users/result"; // 결과 페이지로 리다이렉트
    }

    @DeleteMapping("/me")
    public String deleteUser(Model model) {
        boolean isDeleted = userService.deleteUser();
        if (isDeleted) {
            model.addAttribute("message", "회원 탈퇴 성공!");
        } else {
            model.addAttribute("message", "회원 탈퇴 실패!");
        }
        return "users/result"; // 결과 페이지로 리다이렉트
    }

    @GetMapping("/{userid}")
    public String getUserById(@PathVariable("userid") String userid, Model model) {
        User user = userService.getUserById(userid);
        if (user != null) {
            model.addAttribute("user", user);
            return "users/userDetail"; // 사용자 상세 정보 페이지 (userDetail.html) 렌더링
        } else {
            model.addAttribute("message", "사용자를 찾을 수 없습니다.");
            return "users/result";
        }
    }

}

