package com.example.HomeSaveHome.energy.service;

import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
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
    public YearlyEnergyUsedResponse getYearlyEnergyUsed(Long userId, Long energyId, int year) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("잘못된 유저입니다.");
        }
        if (energyId == null || energyId <= 0) {
            throw new IllegalArgumentException("잘못된 에너지입니다.");
        }

        try {
            List<Object[]> results = energyUsedRepository.getYearlyEnergyUsedAndPrice(userId, energyId, year);

            if (results.isEmpty()) {
                return new YearlyEnergyUsedResponse(energyId, year, 0.0, 0L);
            }

            Object[] result = results.get(0);
            double totalAmount = ((Number) result[0]).doubleValue();
            long totalPrice = ((Number) result[1]).longValue();

            return new YearlyEnergyUsedResponse(energyId, year, totalAmount, totalPrice);
        } catch (Exception e) {
            throw new RuntimeException("연간 에너지 사용량 조회 중 예상치 못한 오류가 발생했습니다.", e);
        }

    }
}
