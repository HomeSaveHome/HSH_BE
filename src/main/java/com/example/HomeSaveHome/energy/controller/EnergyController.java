package com.example.HomeSaveHome.energy.controller;

import com.example.HomeSaveHome.energy.entity.Energy;
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

    @GetMapping("/{energyName}")
    public ResponseEntity<Energy> getEnergyByName(@PathVariable String energyName) {
        Optional<Energy> energy = energyService.getEnergyByName(energyName);
        return energy.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Energy> addEnergy(@RequestBody Energy energy) {
        return ResponseEntity.ok(energyService.saveEnergy(energy));
    }
}
