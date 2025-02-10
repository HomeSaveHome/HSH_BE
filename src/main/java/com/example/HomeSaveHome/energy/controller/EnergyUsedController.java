package com.example.HomeSaveHome.energy.controller;

import com.example.HomeSaveHome.energy.dto.EnergyUsedRequest;
import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.entity.EnergyUsed;
import com.example.HomeSaveHome.energy.service.EnergyUsedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
