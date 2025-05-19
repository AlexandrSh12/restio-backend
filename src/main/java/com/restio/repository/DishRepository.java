package com.restio.repository;

import com.restio.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
    // ничего не нужно писать — все методы уже есть!
}
