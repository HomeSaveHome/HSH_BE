package com.example.HomeSaveHome.energy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Energy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 에너지 ID

    @Column(nullable=false, length=100, unique=true)
    private String energyName;  // 에너지 이름 (전기, 가스 ... )
}
