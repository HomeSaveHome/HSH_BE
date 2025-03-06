package com.example.HomeSaveHome.energy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyEnergyUsedResponse {
    private Long id;
    private String energyName;
    private int year;
    private int month;
    private Double amount;
    private Long price;
}
