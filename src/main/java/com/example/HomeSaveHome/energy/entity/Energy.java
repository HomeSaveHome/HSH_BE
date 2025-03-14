package com.example.HomeSaveHome.energy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Energy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 에너지 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=100, unique=true)
    private EnergyType energyType;  // 에너지 이름 (전기, 가스 ... )

    @OneToMany(mappedBy = "energy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnergyUsed> energyUsedList;
}
