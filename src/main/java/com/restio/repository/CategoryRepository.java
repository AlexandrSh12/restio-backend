package com.restio.repository;

import com.restio.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Базовых методов JpaRepository достаточно
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
}