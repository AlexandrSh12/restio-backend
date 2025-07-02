// OrderItemRepository.java
package com.restio.repository;

import com.restio.model.OrderItem;
import com.restio.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Поиск позиций заказа по блюду (для статистики)
    List<OrderItem> findByDish(Dish dish);
    List<OrderItem> findByDishId(Long dishId);

    // Статистика по популярным блюдам в активной смене
    @Query("SELECT oi FROM OrderItem oi WHERE oi IN " +
            "(SELECT item FROM Order o JOIN o.items item JOIN o.shift s WHERE s.status = 'active')")
    List<OrderItem> findOrderItemsInActiveShift();

    // Получить все позиции заказов по статусу заказа
    @Query("SELECT oi FROM OrderItem oi WHERE oi IN " +
            "(SELECT item FROM Order o JOIN o.items item WHERE o.status = :orderStatus)")
    List<OrderItem> findByOrderStatus(@Param("orderStatus") String orderStatus);
}
