package com.example.HomeSaveHome.energy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnergyUsedRequest {
    private Long energyId;
    private int year;
    private int month;
    private Double amount;
    private Long price;
}
