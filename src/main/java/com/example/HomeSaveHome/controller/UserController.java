package com.example.HomeSaveHome.controller;

import com.example.HomeSaveHome.model.User;
import com.example.HomeSaveHome.service.UserService;
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
        return "result";  // /src/main/resources/templates/result.html로 렌더링
    }

    // GET 방식 : 회원가입 페이지 제공 (나중에 제거해야할 것)
    @GetMapping("/signup")
    public String getSignupForm() {
        return "signup";  // /src/main/resources/templates/signup.html로 렌더링
    }

    // POST 방식 : 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        boolean isSuccess = userService.authenticateUser(username, password);
        if (isSuccess) {
            return "로그인 성공";
        }
        else{
            return "로그인 실패!";
        }
    }

    // GET 방식 : 로그인 페이지 제공 (나중에 프론트랑 합칠 때 제거할 예정)
    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }
}
