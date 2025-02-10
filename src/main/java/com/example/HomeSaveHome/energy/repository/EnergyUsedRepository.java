package com.example.HomeSaveHome.energy.repository;

import com.example.HomeSaveHome.energy.entity.EnergyUsed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnergyUsedRepository extends JpaRepository<EnergyUsed, Long> {
    List<EnergyUsed> findByUserIdAndEnergy_Id(Long userId, Long energyId);
}
