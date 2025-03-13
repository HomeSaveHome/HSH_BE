package com.example.HomeSaveHome.user.controller;

import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입 페이지 요청
    @GetMapping("/signup")
    public String showSignUpForm() {
        return "users/signup";  // 회원가입 폼 페이지로 이동
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
        return "users/result";  // 결과 페이지로 이동
    }

    // 로그인 페이지 요청
    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";  // 로그인 폼 페이지로 이동
    }

    // POST 방식 : 로그인 처리
//    @PostMapping("/login")
//    public String login(@RequestParam String email, @RequestParam String password, Model model) {
//        // 첫 번째로 authenticateUser로 인증만 먼저 수행
//        boolean isAuthenticated = userService.authenticateUser(email, password);
//
//        if (isAuthenticated) {
//            // 두 번째로 authenticateAndSetContext로 SecurityContext에 설정
//            if (userService.authenticateAndSetContext(email, password)) {
//                System.out.println("로그인 성공");
//                return "redirect:/main";  // 로그인 성공 시
//            }
//        } else {
//            model.addAttribute("error", "Invalid email or password");
//            return "login";  // 로그인 실패 시
//        }
//
//        model.addAttribute("error", "Authentication failed");
//        return "login";  // 인증 실패 시
//    }

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
    @PostMapping("/me")
    public String updateProfile(@RequestParam String username,
                                @RequestParam String email,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "users/login";
        }

        User loggedInUser = userService.getLoggedInUser();

        try {
            loggedInUser.setUsername(username);
            loggedInUser.setEmail(email);
            User updatedUser = userService.updateUserProfile(loggedInUser);

            model.addAttribute("user", updatedUser);
            model.addAttribute("message", "프로필이 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());  // 오류 메시지 추가
            return "users/profile";  // 에러 발생 시 기존 프로필 페이지로 돌아감
        } catch (Exception e) {
            model.addAttribute("error", "알 수 없는 오류가 발생했습니다. 다시 시도해주세요.");
            return "users/profile";  // 기타 예외 발생 시에도 프로필 페이지 유지
        }

        return "users/profile";
    }





    // 사용자 삭제
    @PostMapping("/delete")
    public String deleteUser(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 현재 로그인된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "users/login";  // 로그인 페이지로 이동
        }

        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser == null) {
            model.addAttribute("error", "사용자 정보를 찾을 수 없습니다.");
            return "users/login";  // 로그인 페이지로 이동
        }

        // 사용자 삭제 처리
        boolean isDeleted = userService.deleteUser(loggedInUser.getId());
        if (isDeleted) {
            // 세션 및 인증 정보 삭제 (강제 로그아웃)
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();  // 세션 무효화
            }
            SecurityContextHolder.clearContext();  // Spring Security 컨텍스트 초기화

            model.addAttribute("message", "계정이 성공적으로 삭제되었습니다.");
            return "redirect:/onboarding";  // 온보딩 페이지로 리다이렉트
        } else {
            model.addAttribute("error", "계정 삭제에 실패했습니다. 다시 시도해주세요.");
            return "users/profile";  // 프로필 페이지로 이동
        }
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        System.out.println("실행");
        try {
            // SecurityContext에서 인증된 사용자 정보를 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 인증되지 않은 사용자일 경우 로그인 페이지로 리다이렉트
            if (authentication == null || !authentication.isAuthenticated() ) {
                System.out.println("인증오류");
                model.addAttribute("error", "로그인된 사용자가 없습니다. 다시 로그인해주세요.");
                return "users/login";  // 로그인 페이지로 리디렉션
            }
            System.out.println(authentication);

            // 로그인된 사용자 정보 가져오기
            User loggedInUser = userService.getLoggedInUser();

            // 모델에 사용자 정보 추가
            model.addAttribute("user", loggedInUser);

            return "users/profile";  // 프로필 페이지로 이동
        } catch (RuntimeException e) {
            // 예외 처리: 로그인되지 않은 사용자일 경우 로그인 페이지로 리디렉션
            System.out.println("런타임오류");
            model.addAttribute("error", "로그인된 사용자가 없습니다. 다시 로그인해주세요.");
            return "users/login";  // 로그인 페이지로 리디렉션
        }
    }
}