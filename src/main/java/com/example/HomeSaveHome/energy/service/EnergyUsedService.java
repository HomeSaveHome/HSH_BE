package com.example.HomeSaveHome.energy.service;

import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.MonthlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.YearlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyUsed;
import com.example.HomeSaveHome.energy.repository.EnergyRepository;
import com.example.HomeSaveHome.energy.repository.EnergyUsedRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<EnergyUsedResponse> getEnergyUsedByUserAndEnergyId(Long userId, Long energyId) {
        List<EnergyUsed> energyUsedList = energyUsedRepository.findByUserIdAndEnergy_Id(userId, energyId);

        return energyUsedList.stream()
                .map(energyUsed -> new EnergyUsedResponse(
                        energyUsed.getId(),
                        energyUsed.getEnergy().getId(),
                        energyUsed.getYear(),
                        energyUsed.getMonth(),
                        energyUsed.getAmount(),
                        energyUsed.getPrice()
                ))
                .collect(Collectors.toList());
    }

    // 연도별 총 사용량과 가격 계산
    public List<YearlyEnergyUsedResponse> getYearlyEnergyUsed(Long userId, Long energyId) {
        Energy energy = energyRepository.findById(energyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 에너지 타입을 찾을 수 없습니다."));

        List<Object[]> results = energyUsedRepository.getYearlyEnergyUsed(userId, energyId);

        return results.stream()
                .map(result -> new YearlyEnergyUsedResponse(
                        (String) result[0],
                        (int) result[1],
                        ((Number) result[2]).doubleValue(),
                        ((Number) result[3]).longValue()
                ))
                .collect(Collectors.toList());

    }

    // 특정 월 에너지 사용량 조회
    public List<MonthlyEnergyUsedResponse> getEnergyUsedByMonth(Long userId, Long energyId, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("월은 1~12 사이의 값이어야 합니다.");
        }

        Energy energy = energyRepository.findById(energyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 에너지 타입을 찾을 수 없습니다."));

        List<EnergyUsed> energyUsedList = energyUsedRepository.findByUserIdAndEnergyAndMonth(userId, energy, month);

        return energyUsedList.stream()
                .map(e -> new MonthlyEnergyUsedResponse(
                        e.getId(),
                        e.getEnergy().getEnergyName(),
                        e.getYear(),
                        e.getMonth(),
                        e.getAmount(),
                        e.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
