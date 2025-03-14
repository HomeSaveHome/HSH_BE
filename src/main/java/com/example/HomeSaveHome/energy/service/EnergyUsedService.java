package com.example.HomeSaveHome.energy.service;

import com.example.HomeSaveHome.energy.dto.EnergyUsedRequest;
import com.example.HomeSaveHome.energy.dto.EnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.MonthlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.dto.YearlyEnergyUsedResponse;
import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyType;
import com.example.HomeSaveHome.energy.entity.EnergyUsed;
import com.example.HomeSaveHome.user.model.User;
import com.example.HomeSaveHome.energy.repository.EnergyUsedRepository;
import com.example.HomeSaveHome.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnergyUsedService {

    private final EnergyUsedRepository energyUsedRepository;
    private final EnergyService energyService;
    private final UserRepository userRepository;

    public EnergyUsedService(EnergyUsedRepository energyUsedRepository, EnergyService energyService, UserRepository userRepository) {
        this.energyUsedRepository = energyUsedRepository;
        this.energyService = energyService;
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
    public List<MonthlyEnergyUsedResponse> getEnergyUsedByMonth(Long userId, int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("월은 1~12 사이의 값이어야 합니다.");
        }

        User user = getUserById(userId);
        Energy gasEnergy = getEnergyById(1L);
        Energy electricityEnergy = getEnergyById(2L);

        List<MonthlyEnergyUsedResponse> energyUsedList = new ArrayList<>();

        // 가스 사용량 조회
        List<EnergyUsed> gasEnergyUsed = energyUsedRepository.findByUserAndEnergyAndMonthAndYear(user, gasEnergy, month, year);
        if (gasEnergyUsed.isEmpty()) {
            energyUsedList.add(new MonthlyEnergyUsedResponse(null, EnergyType.GAS, year, month, 0.0, null));
        } else {
            energyUsedList.addAll(
                    gasEnergyUsed.stream()
                            .map(e -> new MonthlyEnergyUsedResponse(e.getId(), EnergyType.GAS, year, month, e.getAmount(), e.getPrice()))
                            .toList()
            );
        }

        // 전기 사용량 조회
        List<EnergyUsed> electricityEnergyUsed = energyUsedRepository.findByUserAndEnergyAndMonthAndYear(user, electricityEnergy, month, year);
        if (electricityEnergyUsed.isEmpty()) {
            energyUsedList.add(new MonthlyEnergyUsedResponse(null, EnergyType.ELECTRICITY, year, month, 0.0, null));
        } else {
            energyUsedList.addAll(
                    electricityEnergyUsed.stream()
                            .map(e -> new MonthlyEnergyUsedResponse(e.getId(), EnergyType.ELECTRICITY, year, month, e.getAmount(), e.getPrice()))
                            .toList()
            );
        }

        return energyUsedList;
    }

    // 연도별 총 사용량과 가격 계산
    public List<YearlyEnergyUsedResponse> getYearlyEnergyUsed(Long userId, Long energyId, int year) {
        User user = getUserById(userId);

        List<Object[]> energyUsedList = energyUsedRepository.findByUserAndYear(user, year);
        List<YearlyEnergyUsedResponse> yearlyEnergyUsedList = new ArrayList<>();

        for (Object[] objects : energyUsedList) {
            EnergyType energyType = (EnergyType) objects[0];
            Integer responseYear = (Integer) objects[1];
            Double totalAmount = (Double) objects[2];
            Long totalPrice = (Long) objects[3];

            yearlyEnergyUsedList.add(new YearlyEnergyUsedResponse(energyType, responseYear, totalAmount, totalPrice));
        }

        if (yearlyEnergyUsedList.stream().noneMatch(r -> r.getEnergyType() == EnergyType.GAS)) {
            yearlyEnergyUsedList.add(new YearlyEnergyUsedResponse(EnergyType.GAS, year, 0.0, null));
        }
        if (yearlyEnergyUsedList.stream().noneMatch(r -> r.getEnergyType() == EnergyType.ELECTRICITY)) {
            yearlyEnergyUsedList.add(new YearlyEnergyUsedResponse(EnergyType.ELECTRICITY, year, 0.0, null));
        }

        yearlyEnergyUsedList.sort(Comparator.comparing(r -> r.getEnergyType() == EnergyType.GAS ? 0 : 1));

        return yearlyEnergyUsedList;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    }

    private Energy getEnergyById(Long energyId) {
        return energyService.getEnergyById(energyId)
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

            List<MonthlyEnergyUsedResponse> responseList = getEnergyUsedByMonth(userId, monthValue, yearValue);

            Map<String, MonthlyEnergyUsedResponse> monthData = new HashMap<>();
            for (MonthlyEnergyUsedResponse response : responseList) {
                monthData.put(response.getEnergyType().name(), response);
            }

            last4MonthsEnergy.put(targetMonth.getMonth().toString().substring(0, 3), monthData);
        }

        return last4MonthsEnergy;
    }

    // 변화율 계산
    public Map<EnergyType, Double> getUsageChangeRate(Long userId, List<MonthlyEnergyUsedResponse> currentMonthData) {
        if (currentMonthData.isEmpty()) {
            return Collections.emptyMap();
        }

        List<MonthlyEnergyUsedResponse> previousYearData = getEnergyUsedByMonth(userId, currentMonthData.get(0).getMonth(), currentMonthData.get(0).getYear() - 1);

        for (MonthlyEnergyUsedResponse data : previousYearData) {
            System.out.println("Previous Data - Type: " + data.getEnergyType() + ", Price: " + data.getPrice());
        }

        Long gasUsageCurrent = currentMonthData.get(0).getPrice();
        Long electricityUsageCurrent = currentMonthData.get(1).getPrice();
        Long gasUsagePrevious = null;
        Long electricityUsagePrevious = null;

        for (MonthlyEnergyUsedResponse data : previousYearData) {
            if (EnergyType.GAS.equals(data.getEnergyType())) {
                gasUsagePrevious = data.getPrice();
            } else if (EnergyType.ELECTRICITY.equals(data.getEnergyType())) {
                electricityUsagePrevious = data.getPrice();
            }
        }

        System.out.println("gasUsageCurrent: " + gasUsageCurrent);
        System.out.println("eleUsageCurrent: " + electricityUsageCurrent);
        System.out.println("gasUsagePrevious: " + gasUsagePrevious);
        System.out.println("eleUsagePrevious: " + electricityUsagePrevious);

        return getEnergyTypeDoubleMap(gasUsageCurrent, electricityUsageCurrent, gasUsagePrevious, electricityUsagePrevious);
    }

    private double calculateChangeRate(Long current, Long previous) {
        if (current == null || previous == null || previous == 0) {
            return 0.0;
        }

        double rate = ((double) (current - previous) / previous) * 100;

        BigDecimal roundedRate = new BigDecimal(rate).setScale(1, RoundingMode.HALF_UP);

        return roundedRate.doubleValue();
    }

    // 특정 연도 월 평균 사용량 계산
    public Map<EnergyType, Long> getEvgUsed(Long userId, Long energyId, int year) {
        User user = getUserById(userId);

        List<Object[]> resultList;
        if (energyId != null) {
            Energy energy = getEnergyById(energyId);
            resultList = energyUsedRepository.getYearlyAvgEnergyUsed(user, energy, year);
        } else {
            resultList = energyUsedRepository.getYearlyAvgEnergyUsed(user, null, year);
        }

        return resultList.stream()
                .collect(Collectors.toMap(
                        row -> (EnergyType) row[0],
                        row -> ((Number) row[1]).longValue()
                ));
    }

    // 특정 연도 사용 변화율 계산
    public Map<EnergyType, Double> getYearlyUsedChangeRate(Long userId, int year, List<YearlyEnergyUsedResponse> currentYearData) {
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
        return getEnergyTypeDoubleMap(gasUsageCurrent, electricityUsageCurrent, gasUsagePrevious, electricityUsagePrevious);
    }

    // 월 평균 변화율 계산
    public Map<EnergyType, Double> getYearlyAvgUsedChangeRate(Long userId, int year, Map<EnergyType, Long> currentAvgData) {
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

        return getEnergyTypeDoubleMap(gasAvgUsageCurrent, electricityAvgUsageCurrent, gasAvgUsagePrevious, electricityAvgUsagePrevious);
    }

    private Map<EnergyType, Double> getEnergyTypeDoubleMap(Long gasAvgUsageCurrent, Long electricityAvgUsageCurrent, Long gasAvgUsagePrevious, Long electricityAvgUsagePrevious) {
        double gasChangeRate = calculateChangeRate(gasAvgUsageCurrent, gasAvgUsagePrevious);
        double electricityChangeRate = calculateChangeRate(electricityAvgUsageCurrent, electricityAvgUsagePrevious);

        Map<EnergyType, Double> result = new HashMap<>();
        result.put(EnergyType.GAS, gasChangeRate);
        result.put(EnergyType.ELECTRICITY, electricityChangeRate);

        Map<EnergyType, Double> sortedMap = new LinkedHashMap<>();
        if (result.containsKey(EnergyType.GAS)) {
            sortedMap.put(EnergyType.GAS, result.get(EnergyType.GAS));
        }
        if (result.containsKey(EnergyType.ELECTRICITY)) {
            sortedMap.put(EnergyType.ELECTRICITY, result.get(EnergyType.ELECTRICITY));
        }

        for (Map.Entry<EnergyType, Double> data : sortedMap.entrySet()) {
            System.out.println(data.getKey() + " " + data.getValue());
        }

        return sortedMap;
    }

    public List<Integer> getUsedMonthsByYear(Long userId, int year, Long energyId) {
        User user = getUserById(userId);
        Energy energy = getEnergyById(energyId);
        return energyUsedRepository.findUsedMonthsByUserAndYear(user, year, energy);
    }
}
