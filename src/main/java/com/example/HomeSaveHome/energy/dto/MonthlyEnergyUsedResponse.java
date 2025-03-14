package com.example.HomeSaveHome.energy.dto;

import com.example.HomeSaveHome.energy.entity.EnergyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyEnergyUsedResponse {
    private Long id;
    private EnergyType energyType;
    private int year;
    private int month;
    private Double amount;
    private Long price;
}
