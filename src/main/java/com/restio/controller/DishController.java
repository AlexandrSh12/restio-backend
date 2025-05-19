package com.restio.controller;

import com.restio.dto.DishDTO;
import com.restio.model.Dish;
import com.restio.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    public Dish getDishById(@PathVariable Long id) {
        return dishService.getDishById(id);
    }

    @GetMapping("/category/{categoryName}")
    public List<Dish> getDishesByCategory(@PathVariable String categoryName) {
        return dishService.getDishesByCategory(categoryName);
    }

    @PostMapping
    public Dish createDish(@RequestBody DishDTO dishDTO) {
        return dishService.createDish(dishDTO);
    }

    @PutMapping("/{id}")
    public Dish updateDish(@PathVariable Long id, @RequestBody DishDTO dishDTO) {
        return dishService.updateDish(id, dishDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.ok().build();
    }
}