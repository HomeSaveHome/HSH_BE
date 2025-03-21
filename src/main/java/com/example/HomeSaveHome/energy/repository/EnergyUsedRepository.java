package com.example.HomeSaveHome.energy.repository;

import com.example.HomeSaveHome.energy.entity.Energy;
import com.example.HomeSaveHome.energy.entity.EnergyUsed;
import com.example.HomeSaveHome.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnergyUsedRepository extends JpaRepository<EnergyUsed, Long> {
    List<EnergyUsed> findByUserIdAndEnergyId(Long userId, Long energyId);

    List<EnergyUsed> findByUserAndEnergyAndMonthAndYear(User user, Energy energy, int month, int year);

    @Query("SELECT e.energy.energyType, e.year, SUM(e.amount), SUM(e.price) " +
            "FROM EnergyUsed e " +
            "WHERE e.user = :user " +
            "AND e.year = :year " +
            "GROUP BY e.energy.energyType, e.year ")
    List<Object[]> findByUserAndYear(@Param("user") User user, @Param("year") int year);

    @Query("SELECT e.energy.energyType, AVG(e.price) " +
            "FROM EnergyUsed e " +
            "WHERE e.user = :user " +
            "AND e.year = :year " +
            "AND (:energy IS NULL OR e.energy = :energy) " +
            "GROUP BY e.energy.energyType, e.year " +
            "ORDER BY e.year ASC")
    List<Object[]> getYearlyAvgEnergyUsed(User user, Energy energy, int year);

    @Query("SELECT DISTINCT e.month FROM EnergyUsed e WHERE e.user = :user AND e.year = :year AND e.energy = :energy")
    List<Integer> findUsedMonthsByUserAndYear(User user, int year, Energy energy);
}
