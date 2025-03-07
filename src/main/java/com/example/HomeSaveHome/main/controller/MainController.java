package com.example.HomeSaveHome.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() { //인자로 로그인되어있는지 모델에 넣어서
        return "mainpage/main";
    }
}