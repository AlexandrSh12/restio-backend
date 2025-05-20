package com.restio.repository;

import com.restio.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    // Добавляем поиск по категории
    List<Dish> findByCategory(String category);
}