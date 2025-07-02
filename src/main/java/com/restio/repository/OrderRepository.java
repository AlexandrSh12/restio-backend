// OrderRepository.java
package com.restio.repository;

import com.restio.model.Order;
import com.restio.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    // Поиск заказов по смене
    List<Order> findByShift(Shift shift);

    // Поиск заказов по ID смены
    List<Order> findByShiftId(Long shiftId);

    // Поиск заказов по статусу
    List<Order> findByStatus(String status);

    // Поиск заказов по смене и статусу
    List<Order> findByShiftAndStatus(Shift shift, String status);
    List<Order> findByShiftIdAndStatus(Long shiftId, String status);

    // Поиск заказов текущей активной смены
    @Query("SELECT o FROM Order o JOIN o.shift s WHERE s.status = 'active'")
    List<Order> findOrdersInActiveShift();

    // Поиск заказов текущей активной смены по статусу
    @Query("SELECT o FROM Order o JOIN o.shift s WHERE s.status = 'active' AND o.status = :status")
    List<Order> findOrdersInActiveShiftByStatus(@Param("status") String status);

    // Поиск заказов для кухни (отправленные на приготовление)
    @Query("SELECT o FROM Order o JOIN o.shift s WHERE s.status = 'active' AND (o.status = 'submitted' OR o.status = 'preparing')")
    List<Order> findOrdersForKitchen();

    // Поиск готовых заказов
    @Query("SELECT o FROM Order o JOIN o.shift s WHERE s.status = 'active' AND o.status = 'ready'")
    List<Order> findReadyOrders();

    // Поиск заказа по номеру в смене
    Optional<Order> findByShiftAndOrderNumber(Shift shift, Integer orderNumber);
    Optional<Order> findByShiftIdAndOrderNumber(Long shiftId, Integer orderNumber);
}