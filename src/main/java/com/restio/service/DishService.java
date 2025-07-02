package com.restio.service;

import com.restio.dto.DishDTO;
import com.restio.model.Category;
import com.restio.model.Dish;
import com.restio.repository.CategoryRepository;
import com.restio.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;

    // Оставляем только один конструктор
    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<DishDTO> getAllDishes() {
        return dishRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DishDTO> getDishesByCategory(String categoryName) {
        // Сначала находим категорию по имени
        Optional<Category> category = categoryRepository.findByName(categoryName);
        if (category.isEmpty()) {
            return List.of(); // Возвращаем пустой список, если категория не найдена
        }

        return dishRepository.findByCategory(category.get()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<DishDTO> getDishById(Long id) {
        return dishRepository.findById(id)
                .map(this::convertToDTO);
    }

    public DishDTO createDish(DishDTO dishDTO) {
        Dish dish = convertToEntity(dishDTO);
        Dish savedDish = dishRepository.save(dish);
        return convertToDTO(savedDish);
    }

    public Optional<DishDTO> updateDish(Long id, DishDTO dishDTO) {
        if (!dishRepository.existsById(id)) {
            return Optional.empty();
        }
        Dish dish = convertToEntity(dishDTO);
        dish.setId(id);
        Dish updatedDish = dishRepository.save(dish);
        return Optional.of(convertToDTO(updatedDish));
    }

    public boolean deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            return false;
        }
        dishRepository.deleteById(id);
        return true;
    }

    // Преобразование Entity в DTO
    private DishDTO convertToDTO(Dish dish) {
        DishDTO dto = new DishDTO();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setCategoryId(dish.getCategory().getId());
        dto.setCategoryName(dish.getCategory().getName());
        dto.setPrice(dish.getPrice());
        dto.setCookTime(dish.getCookTime());
        dto.setDescription(dish.getDescription());
        dto.setAvailable(dish.isAvailable());
        dto.setImageUrl(dish.getImageUrl());
        return dto;
    }

    // Преобразование DTO в Entity
    private Dish convertToEntity(DishDTO dto) {
        Dish dish = new Dish();
        if (dto.getId() != null) {
            dish.setId(dto.getId());
        }
        dish.setName(dto.getName());

        // Получаем Category по ID из DTO
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
            dish.setCategory(category);
        }

        dish.setPrice(dto.getPrice());
        dish.setCookTime(dto.getCookTime());
        dish.setDescription(dto.getDescription());
        dish.setAvailable(dto.isAvailable());
        dish.setImageUrl(dto.getImageUrl());
        return dish;
    }
}