package com.example.HomeSaveHome.energy.repository;

import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnergyUsedRepository extends JpaRepository<EnergyUsed, Long> {
    List<EnergyUsed> findByUserIdAndEnergy_Id(Long userId, Long energyId);

    @Query("SELECT e.energy.energyName, e.year, SUM(e.amount), SUM(e.price) " +
            "FROM EnergyUsed e " +
            "WHERE e.userId = :userId AND e.energy.id = :energyId " +
            "GROUP BY e.year " +
            "ORDER BY e.year ASC")
    List<Object[]> getYearlyEnergyUsed(@Param("userId") Long userId,
                                       @Param("energyId") Long energyId);


    List<EnergyUsed> findByUserIdAndEnergyAndMonth(Long userId, Energy energy, int month);
}
