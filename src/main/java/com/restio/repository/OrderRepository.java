package com.restio.repository;

import com.restio.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
    // Изменен тип ID с Long на String, т.к. в модели Order используется String id
}