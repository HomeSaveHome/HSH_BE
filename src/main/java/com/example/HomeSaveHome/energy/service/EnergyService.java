package com.example.HomeSaveHome.energy.service;

import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyType;
import com.example.HomeSaveHome.energy.repository.EnergyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnergyService {

    private final EnergyRepository energyRepository;

    public EnergyService(EnergyRepository energyRepository) {
        this.energyRepository = energyRepository;
    }

    public List<Energy> getAllEnergy() {
        return energyRepository.findAll();
    }

    public Optional<Energy> getEnergyByType(EnergyType energyType) {
        return energyRepository.findByEnergyType(energyType);
    }

    public Optional<Energy> getEnergyById(Long id) {
        return energyRepository.findById(id);
    }

    public Energy saveEnergy(Energy energy) {
        return energyRepository.save(energy);
    }
}
