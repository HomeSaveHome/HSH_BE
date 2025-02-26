package com.example.HomeSaveHome.energy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnergyUsedResponse {
    private Long id;
    private Long energyId;
    private int year;
    private int month;
    private Double amount;
    private Long price;

    public EnergyUsedResponse(Long id, Long energyId, int year, int month, Double amount, Long price) {
        this.id = id;
        this.energyId = energyId;
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.price = price;
    }
}
