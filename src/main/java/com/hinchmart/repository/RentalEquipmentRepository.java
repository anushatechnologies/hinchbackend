package com.hinchmart.repository;

import com.hinchmart.entity.RentalEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalEquipmentRepository extends JpaRepository<RentalEquipment, Long> {

    List<RentalEquipment> findByIsActiveTrueOrderByCategoryAscDailyRateAsc();

    List<RentalEquipment> findByCategoryAndIsActiveTrue(String category);

    Optional<RentalEquipment> findByEquipmentCode(String equipmentCode);
}
