package com.example.HomeSaveHome.energy.config;

import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyType;
import com.example.HomeSaveHome.energy.repository.EnergyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class EnergyInitializer {

    private final EnergyRepository energyRepository;

    public EnergyInitializer(EnergyRepository energyRepository) {
        this.energyRepository = energyRepository;
    }

    @PostConstruct
    public void initEnergyTypes() {
        for (EnergyType type : EnergyType.values()) {
            if (energyRepository.findByEnergyType(type).isEmpty()) {
                Energy energy = new Energy();
                energy.setEnergyType(type);
                energyRepository.save(energy);
            }
        }
    }
}
