package com.example.HomeSaveHome.energy.controller;

import com.example.HomeSaveHome.energy.dto.EnergyUsedRequest;
import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.MonthlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.YearlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.service.EnergyUsedService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/energyUsed")
public class EnergyUsedController {

    private final EnergyUsedService energyUsedService;

    public EnergyUsedController(EnergyUsedService energyUsedService) {
        this.energyUsedService = energyUsedService;
    }

    // 에너지 사용량 입력
    @PostMapping
    public ResponseEntity<EnergyUsedResponse> addEnergyUsage(
            @RequestBody EnergyUsedRequest request) {

        Long userId = 1L;  // 개발용 userId (임시)
        return ResponseEntity.ok(energyUsedService.addEnergyUsed(
                userId, request.getEnergyId(), request.getYear(),
                request.getMonth(), request.getAmount(), request.getPrice()
        ));
    }

    @GetMapping("/{energyId}")
    public ResponseEntity<List<EnergyUsedResponse>> getEnergyUsed(
            @PathVariable Long energyId) {

        Long userId = 1L;  // 개발용 userId (임시)
        List<EnergyUsedResponse> energyUsedResponses = energyUsedService.getEnergyUsedByUserAndEnergyId(userId, energyId);

        return ResponseEntity.ok(energyUsedResponses);
    }

    @GetMapping("/yearly")
    public ResponseEntity<List<YearlyEnergyUsedResponse>> getYearlyEnergyUsed(
            @RequestParam Long energyId) {
        Long userId = 1L;  // 개발용 userId (임시)

        List<YearlyEnergyUsedResponse> response = energyUsedService.getYearlyEnergyUsed(userId, energyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyEnergyUsedResponse>> getMonthlyEnergyUsed(
            @RequestParam Long energyId,
            @RequestParam int month) {
        Long userId = 1L;  // 개발용 userId

        List<MonthlyEnergyUsedResponse> response = energyUsedService.getEnergyUsedByMonth(userId, energyId, month);
        return ResponseEntity.ok(response);
    }

    // 최근 4개월 에너지 사용량 조회
    @GetMapping("/usage-analytics")
    public String getLast4MonthEnergy(@RequestParam Long userId, Model model) {
        Map<String, Map<String, MonthlyEnergyUsedResponse>> usageAnalytics = energyUsedService.getLast4MonthsEnergy(userId);

        int maxGasPrice = usageAnalytics.values().stream()
                .mapToInt(data -> data.getOrDefault("gas", new MonthlyEnergyUsedResponse()).getPrice().intValue())
                .max().orElse(1);

        int maxElectricPrice = usageAnalytics.values().stream()
                .mapToInt(data -> data.getOrDefault("electric", new MonthlyEnergyUsedResponse()).getPrice().intValue())
                .max().orElse(1);

        model.addAttribute("usageAnalytics", usageAnalytics);
        model.addAttribute("maxGasPrice", maxGasPrice);
        model.addAttribute("maxElectricPrice", maxElectricPrice);

        return "main";
    }
}
