package com.restio.service;

import com.restio.dto.DishDTO;
import com.restio.exception.ResourceNotFoundException;
import com.restio.model.Category;
import com.restio.model.Dish;
import com.restio.repository.CategoryRepository;
import com.restio.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish", "id", id));
    }

    public Dish createDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        dish.setName(dishDTO.getName());
        dish.setCategory(dishDTO.getCategory());
        dish.setPrice(dishDTO.getPrice());
        dish.setCookTime(dishDTO.getCookTime());
        return dishRepository.save(dish);
    }

    public Dish updateDish(Long id, DishDTO dishDTO) {
        Dish dish = getDishById(id);
        dish.setName(dishDTO.getName());
        dish.setCategory(dishDTO.getCategory());
        dish.setPrice(dishDTO.getPrice());
        dish.setCookTime(dishDTO.getCookTime());
        return dishRepository.save(dish);
    }

    public void deleteDish(Long id) {
        Dish dish = getDishById(id);
        dishRepository.delete(dish);
    }

    public List<Dish> getDishesByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", categoryName));

        return dishRepository.findAll().stream()
                .filter(dish -> dish.getCategory().equals(categoryName))
                .collect(Collectors.toList());
    }
}