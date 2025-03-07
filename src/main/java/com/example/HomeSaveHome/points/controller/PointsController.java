package com.example.HomeSaveHome.points.controller;

import com.example.HomeSaveHome.points.model.PointTransaction;
import com.example.HomeSaveHome.points.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/points")
public class PointsController {

    @Autowired
    private PointService pointService;

    // 포인트 적립 (POST)
    @PostMapping
    public String addPoints(@RequestParam int amount, Model model) {
        boolean success = pointService.addPoints(amount);
        model.addAttribute("message", success ? "포인트 적립 성공!" : "포인트 적립 실패!");
        return "layout/result";
    }

    // 포인트 적립 내역 조회 (GET)
    @GetMapping("/history")
    public String getPointHistory(Model model) {
        // 포인트 트랜잭션 내역을 조회하여 모델에 추가
        model.addAttribute("history", pointService.getPointHistory());
        return "layouts/pointHistory"; // 포인트 내역 페이지
    }
}
