package com.restio.service;

import com.restio.dto.CategoryDTO;
import com.restio.exception.BadRequestException;
import com.restio.exception.ResourceNotFoundException;
import com.restio.model.Category;
import com.restio.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", name));
    }

    public Category createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new BadRequestException("Category with name " + categoryDTO.getName() + " already exists");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = getCategoryById(id);

        // Проверяем, не занято ли новое имя другой категорией
        if (!category.getName().equals(categoryDTO.getName()) &&
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new BadRequestException("Category with name " + categoryDTO.getName() + " already exists");
        }

        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}