package com.example.HomeSaveHome.energy.repository;

import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnergyRepository extends JpaRepository<Energy, Long> {
    Optional<Energy> findByEnergyType(EnergyType energyType);
}
