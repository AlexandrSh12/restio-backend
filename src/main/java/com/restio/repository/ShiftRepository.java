// ShiftRepository.java
package com.restio.repository;

import com.restio.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    // Найти активную смену
    @Query("SELECT s FROM Shift s WHERE s.status = 'active'")
    Optional<Shift> findActiveShift();

    // Найти текущую активную смену (самую последнюю)
    @Query("SELECT s FROM Shift s WHERE s.status = 'active' ORDER BY s.startDate DESC")
    Optional<Shift> findCurrentActiveShift();

    // Найти смены по статусу
    List<Shift> findByStatus(String status);

    // Найти смены за период
    List<Shift> findByStartDateBetween(LocalDateTime start, LocalDateTime end);

    // Найти закрытые смены за период
    @Query("SELECT s FROM Shift s WHERE s.status = 'closed' AND s.startDate >= :start AND s.endDate <= :end")
    List<Shift> findClosedShiftsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Проверить, есть ли активная смена
    @Query("SELECT COUNT(s) > 0 FROM Shift s WHERE s.status = 'active'")
    boolean hasActiveShift();

    // Инкремент номера заказа в смене
    @Modifying
    @Query("UPDATE Shift s SET s.currentOrderNumber = s.currentOrderNumber + 1 WHERE s.id = :shiftId")
    void incrementOrderNumber(@Param("shiftId") Long shiftId);
}