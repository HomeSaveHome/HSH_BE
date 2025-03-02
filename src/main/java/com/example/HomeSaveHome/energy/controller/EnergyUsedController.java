package com.example.HomeSaveHome.energy.controller;

import com.example.HomeSaveHome.energy.dto.EnergyUsedRequest;
import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.MonthlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.YearlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.service.EnergyUsedService;
import com.example.HomeSaveHome.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
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
    @GetMapping("/input")
    public String showEnergyInputForm(Model model) {
        model.addAttribute("energyUsedRequest", new EnergyUsedRequest());
        return "usage-input";
    }

    @PostMapping("/save")
    public String addEnergyUsage(@AuthenticationPrincipal User user,  // 로그인한 유저 정보 가져오기
                                 @ModelAttribute EnergyUsedRequest request,
                                 RedirectAttributes redirectAttributes) {
        try {
            request.setUserId(user.getId());
            energyUsedService.saveEnergyUsage(request);
            redirectAttributes.addFlashAttribute("successMessage", "에너지 사용량이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "에너지 사용량 저장 중 오류 발생: " + e.getMessage());
        }

        return "redirect:/energy";
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
            @RequestParam int month,
            @RequestParam int year) {
        Long userId = 1L;  // 개발용 userId

        List<MonthlyEnergyUsedResponse> response = energyUsedService.getEnergyUsedByMonth(userId, energyId, month, year);
        return ResponseEntity.ok(response);
    }

    // 저번 달 에너지 사용량 조회
    @GetMapping("/last-month")
    public String getLastMonthEnergyUsed(@RequestParam Long userId, Model model) {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);

        List<MonthlyEnergyUsedResponse> lastUsage = energyUsedService.getEnergyUsedByMonth(userId, null, lastMonth.getMonthValue(), lastMonth.getYear());
        model.addAttribute("lastUsage", lastUsage);

        return "main";
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
