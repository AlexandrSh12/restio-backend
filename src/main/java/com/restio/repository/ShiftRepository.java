package com.restio.repository;

import com.restio.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    @Query("SELECT s FROM Shift s WHERE s.status = 'active'")
    Optional<Shift> findActiveShift();

    @Query("SELECT s FROM Shift s WHERE s.status = 'active' ORDER BY s.startDate DESC")
    Optional<Shift> findCurrentActiveShift();
}