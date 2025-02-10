package com.example.HomeSaveHome.energy.controller;

import com.example.HomeSaveHome.energy.dto.EnergyUsedRequest;
import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.service.EnergyUsedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/energyUsed")
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
        List<EnergyUsedResponse> energyUsedResponses = energyUsedService.getEnergyUsageByUserAndEnergyId(userId, energyId);

        return ResponseEntity.ok(energyUsedResponses);
    }
}
