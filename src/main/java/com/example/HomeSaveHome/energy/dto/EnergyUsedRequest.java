package com.example.HomeSaveHome.energy.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyUsedRequest {
    private Long userId;
    private Long energyId;
    private int year;
    private int month;
    private Double amount;
    private Long price;
}
