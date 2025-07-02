package com.restio.repository;

import com.restio.model.Category;
import com.restio.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    // Поиск по объекту категории
    List<Dish> findByCategory(Category category);

    // Поиск только доступных блюд
    List<Dish> findByAvailableTrue();

    // Поиск доступных блюд по категории
    List<Dish> findByCategoryAndAvailableTrue(Category category);

    // Поиск по имени (для поиска блюд)
    List<Dish> findByNameContainingIgnoreCase(String name);

    // Поиск доступных блюд по имени
    List<Dish> findByNameContainingIgnoreCaseAndAvailableTrue(String name);

    // Получить все категории с доступными блюдами
    @Query("SELECT DISTINCT d.category FROM Dish d WHERE d.available = true AND d.category IS NOT NULL")
    List<Category> findDistinctCategoriesWithAvailableDishes();
}