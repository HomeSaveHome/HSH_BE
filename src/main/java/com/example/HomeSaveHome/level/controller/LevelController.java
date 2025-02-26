package com.example.HomeSaveHome.level.controller;

import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LevelController {

    @Autowired
    private UserService userService;

    // 레벨 확인 (GET)
    @GetMapping("/level")
    public String getLevel(Model model) {
        User currentUser = userService.getCurrentUser();
        // User 모델에 level 필드가 있다고 가정
        model.addAttribute("level", currentUser.getLevel());
        return "layouts/level";  // 레벨 정보를 보여주는 페이지
    }
}
