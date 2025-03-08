package com.example.HomeSaveHome.energy.controller;

import com.example.HomeSaveHome.energy.dto.EnergyUsedRequest;
import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.MonthlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.YearlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.entity.EnergyType;
import com.example.HomeSaveHome.energy.service.EnergyUsedService;
import com.example.HomeSaveHome.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // 월 별 조회
    @GetMapping("/monthly")
    public String getMonthlyEnergyUsed(
            @RequestParam Long userId,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            Model model) {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        if (year == null) year = currentYear;
        if (month == null) month = currentMonth;

        // 검색한 월 에너지 사용량 데이터 가져오기
        List<MonthlyEnergyUsedResponse> currentMonthData = energyUsedService.getEnergyUsedByMonth(userId, null, month, year);

        // 변화율 데이터 가져오기
        Map<String, Map<String, Optional<Long>>> changeRates = energyUsedService.getUsageChangeRate(userId, currentMonthData);

        model.addAttribute("currentMonthData", currentMonthData);
        model.addAttribute("changeRates", changeRates);

        return "monthly-usage";
    }

    // 연도 별 에너지 사용량 조회
    @GetMapping("/yearly")
    public String getYearlyEnergyUsed(
            @RequestParam Long userId,
            @RequestParam(value = "year", required = false) Integer year,
            Model model) {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();

        if (year == null) year = currentYear;

        // 검색한 년도 에너지 데이터 가져오기
        List<YearlyEnergyUsedResponse> currentYearData = energyUsedService.getYearlyEnergyUsed(userId, null, year);
        // 총 사용량 변화율 데이터 가져오기
        Map<EnergyType, Optional<Long>> yearlyChangeRates = energyUsedService.getYearlyUsedChangeRate(userId, year, currentYearData);

        // 월 평균 데이터 가져오기
        Map<EnergyType, Long> avgUsedMap = energyUsedService.getEvgUsed(userId, null, year);
        // 변화율 데이터 가져오기
        Map<EnergyType, Optional<Long>> avgChangeRates = energyUsedService.getYearlyAvgUsedChangeRate(userId, year, avgUsedMap);

        model.addAttribute("currentYearData", currentYearData);
        model.addAttribute("yearlyChangeRates", yearlyChangeRates);
        model.addAttribute("avgChangeRates", avgChangeRates);

        return "yearly-usage";
    }
}
