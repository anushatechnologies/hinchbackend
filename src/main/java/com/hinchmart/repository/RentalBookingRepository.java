package com.hinchmart.repository;

import com.hinchmart.entity.RentalBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalBookingRepository extends JpaRepository<RentalBooking, Long> {

    List<RentalBooking> findByUserIdOrderByCreatedAtDesc(Long userId);
}
