package com.example.HomeSaveHome.energy.dto;

import com.example.HomeSaveHome.energy.entity.EnergyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YearlyEnergyUsedResponse {
    private EnergyType energyType;
    private int year;
    private Double totalAmount;
    private Long totalPrice;
}
