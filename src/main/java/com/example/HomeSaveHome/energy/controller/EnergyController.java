package com.example.HomeSaveHome.energy.controller;

import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyType;
import com.example.HomeSaveHome.energy.service.EnergyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/energy")
public class EnergyController {

    private final EnergyService energyService;

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }

    @GetMapping
    public ResponseEntity<List<Energy>> getAllEnergy() {
        return ResponseEntity.ok(energyService.getAllEnergy());
    }

    @GetMapping("/{energyType}")
    public ResponseEntity<Energy> getEnergyByType(@PathVariable EnergyType energyType) {
        Optional<Energy> energy = energyService.getEnergyByName(energyType);
        return energy.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Energy> addEnergy(@RequestBody Energy energy) {
        return ResponseEntity.ok(energyService.saveEnergy(energy));
    }
}
