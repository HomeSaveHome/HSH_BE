package com.example.HomeSaveHome.energy.service;

import com.example.HomeSaveHome.energy.dto.EnergyUsedRequest;
import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.MonthlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.YearlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyType;
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
    public void saveEnergyUsage(EnergyUsedRequest request) {
        User user = getUserById(request.getUserId());
        Energy energy = getEnergyById(request.getEnergyId());

        if (request.getAmount() <= 0 || request.getPrice() < 0) {
            throw new IllegalArgumentException("에너지 사용량 또는 가격이 올바르지 않습니다.");
        }

        EnergyUsed energyUsed = EnergyUsed.builder()
                .user(user)
                .energy(energy)
                .year(request.getYear())
                .month(request.getMonth())
                .amount(request.getAmount())
                .price(request.getPrice())
                .build();

        energyUsedRepository.save(energyUsed);
    }

    public List<EnergyUsedResponse> getEnergyUsedByUserAndEnergyId(Long userId, Long energyId) {
        List<EnergyUsed> energyUsedList = energyUsedRepository.findByUserIdAndEnergyId(userId, energyId);

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

    // 특정 월 에너지 사용량 조회
    public List<MonthlyEnergyUsedResponse> getEnergyUsedByMonth(Long userId, Long energyId, int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("월은 1~12 사이의 값이어야 합니다.");
        }

        User user = getUserById(userId);

        List<EnergyUsed> energyUsedList;
        if (energyId != null) {
            Energy energy = getEnergyById(energyId);
            energyUsedList = energyUsedRepository.findByUserAndEnergyAndMonthAndYear(user, energy, month, year);
        } else {
            energyUsedList = energyUsedRepository.findByUserAndEnergyAndMonthAndYear(user, null, month, year);
        }

        return energyUsedList.stream()
                .map(e -> new MonthlyEnergyUsedResponse(
                        e.getId(),
                        e.getEnergy().getEnergyType(),
                        e.getYear(),
                        e.getMonth(),
                        e.getAmount(),
                        e.getPrice()
                ))
                .collect(Collectors.toList());
    }

    // 연도별 총 사용량과 가격 계산
    public List<YearlyEnergyUsedResponse> getYearlyEnergyUsed(Long userId, Long energyId, int year) {
        User user = getUserById(userId);

        List<Object[]> energyUsedList;

        if (energyId != null) {
            Energy energy = getEnergyById(energyId);
            energyUsedList = energyUsedRepository.getYearlyEnergyUsed(user, energy, year);
        } else {
            energyUsedList = energyUsedRepository.getYearlyEnergyUsed(user, null, year);
        }

        return energyUsedList.stream()
                .map(e -> new YearlyEnergyUsedResponse(
                        (EnergyType) e[0],  // energyType
                        (int) e[1], // energyId
                        (Double) e[2], // totalAmount
                        (Long) e[3] // totalPrice
                ))
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    }

    private Energy getEnergyById(Long energyId) {
        return energyRepository.findById(energyId)
                .orElseThrow(() -> new RuntimeException("해당 에너지가 존재하지 않습니다."));
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
                monthData.put(response.getEnergyType().name(), response);
            }

            last4MonthsEnergy.put(targetMonth.getMonth().toString().substring(0, 3), monthData);
        }

        return last4MonthsEnergy;
    }

    // 변화율 계산
    public Map<String, Map<String, Optional<Long>>> getUsageChangeRate(Long userId, List<MonthlyEnergyUsedResponse> currentMonthData) {
        List<MonthlyEnergyUsedResponse> previousYearData = getEnergyUsedByMonth(userId, null, currentMonthData.get(0).getMonth(), currentMonthData.get(0).getYear() - 1);

        Long gasUsageCurrent = null;
        Long electricityUsageCurrent = null;
        Long gasUsagePrevious = null;
        Long electricityUsagePrevious = null;

        for (MonthlyEnergyUsedResponse data : currentMonthData) {
            if ("GAS".equalsIgnoreCase(data.getEnergyType().name())) {
                gasUsageCurrent = data.getPrice();
            } else if ("ELECTRICITY".equalsIgnoreCase(data.getEnergyType().name())) {
                electricityUsageCurrent = data.getPrice();
            }
        }

        for (MonthlyEnergyUsedResponse data : previousYearData) {
            if ("GAS".equalsIgnoreCase(data.getEnergyType().name())) {
                gasUsagePrevious = data.getPrice();
            } else if ("ELECTRICITY".equalsIgnoreCase(data.getEnergyType().name())) {
                electricityUsagePrevious = data.getPrice();
            }
        }

        // 변화율 계산
        Optional<Long> gasChangeRate = calculateChangeRate(gasUsageCurrent, gasUsagePrevious);
        Optional<Long> electricityChangeRate = calculateChangeRate(electricityUsageCurrent, electricityUsagePrevious);
        // 가격 차이 계산
        Optional<Long> gasPriceDiff = calculatePriceDiff(gasUsageCurrent, gasUsagePrevious);
        Optional<Long> electricityPriceDiff = calculatePriceDiff(electricityUsageCurrent, electricityUsagePrevious);

        Map<String, Map<String, Optional<Long>>> result = new HashMap<>();

        Map<String, Optional<Long>> gasData = new HashMap<>();
        gasData.put("changeRate", gasChangeRate);
        gasData.put("priceDiff", gasPriceDiff);

        Map<String, Optional<Long>> electricityData = new HashMap<>();
        gasData.put("changeRate", electricityChangeRate);
        gasData.put("priceDiff", electricityPriceDiff);

        result.put("gas", gasData);
        result.put("electricity", electricityData);

        return result;
    }

    private Optional<Long> calculateChangeRate(Long current, Long previous) {
        if (current == null) return Optional.empty();
        if (previous == null || previous == 0) return Optional.empty();

        return Optional.of(((current - previous) / previous) * 100);
    }

    private Optional<Long> calculatePriceDiff(Long current, Long previous) {
        if (current == null || previous == null) {
            return Optional.empty();
        }
        return Optional.of(current - previous);
    }

    // 특정 연도 월 평균 사용량 계산
    public Map<EnergyType, Long> getEvgUsed(Long userId, Long energyId, int year) {
        User user = getUserById(userId);

        Map<EnergyType, Long> avgUsageMap;
        if (energyId != null) {
            Energy energy = getEnergyById(energyId);
            avgUsageMap = energyUsedRepository.getYearlyAvgEnergyUsed(user, energy, year);
        } else {
            avgUsageMap = energyUsedRepository.getYearlyAvgEnergyUsed(user, null, year);
        }

        return avgUsageMap;
    }

    // 특정 연도 사용 변화율 계산
    public Map<EnergyType, Optional<Long>> getYearlyUsedChangeRate(Long userId, int year, List<YearlyEnergyUsedResponse> currentYearData) {
        List<YearlyEnergyUsedResponse> previousYearData = getYearlyEnergyUsed(userId, null, year - 1);

        Long gasUsageCurrent = null;
        Long electricityUsageCurrent = null;
        Long gasUsagePrevious = null;
        Long electricityUsagePrevious = null;

        for (YearlyEnergyUsedResponse data : currentYearData) {
            if ("GAS".equalsIgnoreCase(data.getEnergyType().name())) {
                gasUsageCurrent = data.getTotalPrice();
            } else if ("ELECTRICITY".equalsIgnoreCase(data.getEnergyType().name())) {
                electricityUsageCurrent = data.getTotalPrice();
            }
        }

        for (YearlyEnergyUsedResponse data : previousYearData) {
            if ("GAS".equalsIgnoreCase(data.getEnergyType().name())) {
                gasUsagePrevious = data.getTotalPrice();
            } else if ("ELECTRICITY".equalsIgnoreCase(data.getEnergyType().name())) {
                electricityUsagePrevious = data.getTotalPrice();
            }
        }

        // 변화율 계산
        Optional<Long> gasChangeRate = calculateChangeRate(gasUsageCurrent, gasUsagePrevious);
        Optional<Long> electricityChangeRate = calculateChangeRate(electricityUsageCurrent, electricityUsagePrevious);

        Map<EnergyType, Optional<Long>> result = new HashMap<>();
        result.put(EnergyType.GAS, gasChangeRate);
        result.put(EnergyType.ELECTRICITY, electricityChangeRate);

        return result;
    }

    // 월 평균 변화율 계산
    public Map<EnergyType, Optional<Long>> getYearlyAvgUsedChangeRate(Long userId, int year, Map<EnergyType, Long> currentAvgData) {
        Map<EnergyType, Long> previousAvgData = getEvgUsed(userId, null, year - 1);

        Long gasAvgUsageCurrent = null;
        Long electricityAvgUsageCurrent = null;
        Long gasAvgUsagePrevious = null;
        Long electricityAvgUsagePrevious = null;

        for (Map.Entry<EnergyType, Long> data : currentAvgData.entrySet()) {
            EnergyType energyType = data.getKey();
            Long totalPrice = data.getValue();

            if ("GAS".equalsIgnoreCase(energyType.name())) {
                gasAvgUsageCurrent = totalPrice;
            } else if ("ELECTRICITY".equalsIgnoreCase(energyType.name())) {
                electricityAvgUsageCurrent = totalPrice;
            }
        }

        for (Map.Entry<EnergyType, Long> data : previousAvgData.entrySet()) {
            EnergyType energyType = data.getKey();
            Long totalPrice = data.getValue();

            if ("GAS".equalsIgnoreCase(energyType.name())) {
                gasAvgUsagePrevious = totalPrice;
            } else if ("ELECTRICITY".equalsIgnoreCase(energyType.name())) {
                electricityAvgUsagePrevious = totalPrice;
            }
        }

        // 변화율 계산
        Optional<Long> gasChangeRate = calculateChangeRate(gasAvgUsageCurrent, gasAvgUsagePrevious);
        Optional<Long> electricityChangeRate = calculateChangeRate(electricityAvgUsageCurrent, electricityAvgUsagePrevious);

        Map<EnergyType, Optional<Long>> result = new HashMap<>();
        result.put(EnergyType.GAS, gasChangeRate);
        result.put(EnergyType.ELECTRICITY, electricityChangeRate);

        return result;
    }
}
