package com.example.HomeSaveHome.energy.service;

import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.MonthlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.YearlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyUsed;
import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.energy.repository.EnergyRepository;
import com.example.HomeSaveHome.energy.repository.EnergyUsedRepository;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnergyUsedService {

    private final EnergyUsedRepository energyUsedRepository;
    private final EnergyRepository energyRepository;
    private final UserRepository userRepository;

    public EnergyUsedService(EnergyUsedRepository energyUsedRepository, EnergyRepository energyRepository, UserRepository userRepository) {
        this.energyUsedRepository = energyUsedRepository;
        this.energyRepository = energyRepository;
        this.userRepository = userRepository;
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
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다.")))
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
    public List<MonthlyEnergyUsedResponse> getEnergyUsedByMonth(Long userId, Long energyId, int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("월은 1~12 사이의 값이어야 합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        List<EnergyUsed> energyUsedList;
        if (energyId != null) {
            Energy energy = energyRepository.findById(energyId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 에너지 타입을 찾을 수 없습니다."));
            energyUsedList = energyUsedRepository.findByUserIdAndEnergyAndMonthAndYear(user, energy, month, year);
        } else {
            energyUsedList = energyUsedRepository.findByUserIdAndEnergyAndMonthAndYear(user, null, month, year);
        }

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

    // 최근 4개월 간의 에너지 사용량 조회
    public Map<String, Map<String, MonthlyEnergyUsedResponse>> getLast4MonthsEnergy(Long userId) {
        YearMonth currentMonth = YearMonth.now();
        Map<String, Map<String, MonthlyEnergyUsedResponse>> last4MonthsEnergy = new LinkedHashMap<>();

        for (int i = 1; i <= 4; i++) {
            YearMonth targetMonth = currentMonth.minusMonths(i);
            int monthValue = targetMonth.getMonthValue();
            int yearValue = targetMonth.getYear();

            List<MonthlyEnergyUsedResponse> responseList = getEnergyUsedByMonth(userId, null, monthValue, yearValue);

            Map<String, MonthlyEnergyUsedResponse> monthData = new HashMap<>();
            for (MonthlyEnergyUsedResponse response : responseList) {
                monthData.put(response.getEnergyName(), response);
            }

            last4MonthsEnergy.put(targetMonth.getMonth().toString().substring(0, 3), monthData);
        }

        return last4MonthsEnergy;
    }
}
