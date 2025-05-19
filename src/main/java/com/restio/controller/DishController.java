package com.restio.controller;

import com.restio.model.Dish;
import com.restio.repository.DishRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishRepository dishRepo;

    public DishController(DishRepository dishRepo) {
        this.dishRepo = dishRepo;
    }

    @GetMapping
    public List<Dish> getAll() {
        return dishRepo.findAll();
    }
}
