package com.example.HomeSaveHome.energy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class YearlyEnergyUsedResponse {
    private Long energyId;
    private int year;
    private double totalAmount;
    private Long totalPrice;
}
