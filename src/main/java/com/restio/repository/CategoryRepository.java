// CategoryRepository.java
package com.restio.repository;

import com.restio.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Существующие методы
    Optional<Category> findByName(String name);
    boolean existsByName(String name);

    // Дополнительные полезные методы
    // Поиск категорий по части имени
    List<Category> findByNameContainingIgnoreCase(String name);

    // Получить все категории, отсортированные по имени
    List<Category> findAllByOrderByNameAsc();

    // Найти категории, которые используются в доступных блюдах
    @Query("SELECT DISTINCT c FROM Category c WHERE c.id IN " +
            "(SELECT d.category.id FROM Dish d WHERE d.available = true AND d.category IS NOT NULL)")
    List<Category> findCategoriesWithAvailableDishes();

    // Подсчет блюд в категории
    @Query("SELECT COUNT(d) FROM Dish d WHERE d.category.id = :categoryId")
    Long countDishesByCategory(@Param("categoryId") Long categoryId);

    // Подсчет доступных блюд в категории
    @Query("SELECT COUNT(d) FROM Dish d WHERE d.category.id = :categoryId AND d.available = true")
    Long countAvailableDishesByCategory(@Param("categoryId") Long categoryId);
}