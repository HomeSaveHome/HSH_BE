package com.example.HomeSaveHome.energy.service;

import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyUsed;
import com.example.HomeSaveHome.energy.repository.EnergyRepository;
import com.example.HomeSaveHome.energy.repository.EnergyUsedRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnergyUsedService {

    private final EnergyUsedRepository energyUsedRepository;
    private final EnergyRepository energyRepository;

    public EnergyUsedService(EnergyUsedRepository energyUsedRepository, EnergyRepository energyRepository) {
        this.energyUsedRepository = energyUsedRepository;
        this.energyRepository = energyRepository;
    }

    // 에너지 사용량 입력
    public EnergyUsedResponse addEnergyUsed(Long userId, Long energyId, int year, int month, double amount, Long price) {
        Optional<Energy> energy = energyRepository.findById(energyId);

        if (energy.isEmpty()) {
            throw new RuntimeException("해당 에너지가 존재하지 않습니다.");
        }

        if (amount <= 0 || price < 0) {
            throw new IllegalArgumentException("에너지 사용량 또는 가격이 올바르지 않습니다.");
        }

        EnergyUsed energyUsed = EnergyUsed.builder()
                .userId(userId)
                .energy(energy.get())
                .year(year)
                .month(month)
                .amount(amount)
                .price(price)
                .build();

        EnergyUsed saved = energyUsedRepository.save(energyUsed);

        return new EnergyUsedResponse(
                saved.getId(), saved.getEnergy().getId(), saved.getYear(),
                saved.getMonth(), saved.getAmount(), saved.getPrice()
        );
    }
}
