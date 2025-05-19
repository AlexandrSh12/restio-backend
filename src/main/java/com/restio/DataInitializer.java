package com.restio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.restio.model.Category;
import com.restio.model.Dish;
import com.restio.model.Role;
import com.restio.model.User;
import com.restio.repository.CategoryRepository;
import com.restio.repository.DishRepository;
import com.restio.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           DishRepository dishRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Создаем пользователей только если репозиторий пуст
        if (userRepository.count() == 0) {
            createUsers();
        }

        // Создаем категории и блюда только если репозиторий категорий пуст
        if (categoryRepository.count() == 0) {
            createCategoriesAndDishes();
        }
    }

    private void createUsers() {
        // Создаем администратора
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setFullName("Administrator");
        admin.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN)));
        admin.setActive(true);
        userRepository.save(admin);

        // Создаем официанта
        User waiter = new User();
        waiter.setUsername("waiter");
        waiter.setPassword(passwordEncoder.encode("waiter"));
        waiter.setFullName("Waiter");
        waiter.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_WAITER)));
        waiter.setActive(true);
        userRepository.save(waiter);

        // Создаем повара
        User chef = new User();
        chef.setUsername("chef");
        chef.setPassword(passwordEncoder.encode("chef"));
        chef.setFullName("Chef");
        chef.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_CHEF)));
        chef.setActive(true);
        userRepository.save(chef);

        System.out.println("Users created successfully");
    }

    private void createCategoriesAndDishes() {
        // Создаем категории
        Category starters = new Category();
        starters.setName("Закуски");
        starters.setDescription("Легкие закуски и аперитивы");
        categoryRepository.save(starters);

        Category mainCourses = new Category();
        mainCourses.setName("Основные блюда");
        mainCourses.setDescription("Основные горячие блюда");
        categoryRepository.save(mainCourses);

        Category desserts = new Category();
        desserts.setName("Десерты");
        desserts.setDescription("Сладкие десерты и выпечка");
        categoryRepository.save(desserts);

        Category drinks = new Category();
        drinks.setName("Напитки");
        drinks.setDescription("Горячие и холодные напитки");
        categoryRepository.save(drinks);

        // Создаем блюда
        // Закуски
        createDish("Цезарь с курицей", "Закуски", 350, 15);
        createDish("Карпаччо из говядины", "Закуски", 420, 10);
        createDish("Греческий салат", "Закуски", 280, 10);

        // Основные блюда
        createDish("Стейк Рибай", "Основные блюда", 950, 25);
        createDish("Паста Карбонара", "Основные блюда", 380, 20);
        createDish("Жареный лосось", "Основные блюда", 650, 25);

        // Десерты
        createDish("Чизкейк", "Десерты", 250, 5);
        createDish("Тирамису", "Десерты", 280, 5);
        createDish("Мороженое", "Десерты", 150, 3);

        // Напитки
        createDish("Кофе американо", "Напитки", 120, 5);
        createDish("Чай зеленый", "Напитки", 100, 5);
        createDish("Сок апельсиновый", "Напитки", 150, 1);

        System.out.println("Categories and dishes created successfully");
    }

    private void createDish(String name, String category, int price, int cookTime) {
        Dish dish = new Dish();
        dish.setName(name);
        dish.setCategory(category);
        dish.setPrice(price);
        dish.setCookTime(cookTime);
        dishRepository.save(dish);
    }
}