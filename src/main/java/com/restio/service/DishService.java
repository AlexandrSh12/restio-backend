package com.restio.service;

import com.restio.dto.DishDTO;
import com.restio.model.Dish;
import com.restio.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<DishDTO> getAllDishes() {
        return dishRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DishDTO> getDishesByCategory(String category) {
        return dishRepository.findByCategory(category).stream()
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
        dto.setCategory(dish.getCategory());
        dto.setPrice(dish.getPrice());
        dto.setCookTime(dish.getCookTime());
        dto.setImageUrl(dish.getImageUrl()); // Добавьте это
        return dto;
    }

    // Преобразование DTO в Entity
    private Dish convertToEntity(DishDTO dto) {
        Dish dish = new Dish();
        if (dto.getId() != null) {
            dish.setId(dto.getId());
        }
        dish.setName(dto.getName());
        dish.setCategory(dto.getCategory());
        dish.setPrice(dto.getPrice());
        dish.setCookTime(dto.getCookTime());
        dish.setImageUrl(dto.getImageUrl()); // Добавьте это
        return dish;
    }
}