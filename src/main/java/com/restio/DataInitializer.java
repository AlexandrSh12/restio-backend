package com.restio;

import com.restio.model.Category;
import com.restio.model.Dish;
import com.restio.model.Role;
import com.restio.model.User;
import com.restio.repository.CategoryRepository;
import com.restio.repository.DishRepository;
import com.restio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepository,
                                   DishRepository dishRepository,
                                   UserRepository userRepository) {
        return args -> {
            // Инициализация тестовых пользователей
            initUsers(userRepository);

            // Инициализация меню
            initMenu(categoryRepository, dishRepository);
        };
    }

    private void initUsers(UserRepository userRepository) {
        // Проверяем, не созданы ли уже пользователи
        if (userRepository.count() > 0) {
            return; // Если пользователи уже существуют, не создаем новых
        }

        // Создаем администратора
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Администратор Системы");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(Role.ADMIN);
        admin.setRoles(adminRoles);
        userRepository.save(admin);

        // Создаем официанта
        User waiter = new User();
        waiter.setUsername("waiter");
        waiter.setPassword(passwordEncoder.encode("waiter123"));
        waiter.setFullName("Иван Официантов");
        waiter.setRoles(Collections.singleton(Role.WAITER));
        userRepository.save(waiter);

        // Создаем повара
        User chef = new User();
        chef.setUsername("chef");
        chef.setPassword(passwordEncoder.encode("chef123"));
        chef.setFullName("Петр Поваренко");
        chef.setRoles(Collections.singleton(Role.CHEF));
        userRepository.save(chef);

        System.out.println("Тестовые пользователи созданы успешно!");
    }

    private void initMenu(CategoryRepository categoryRepository, DishRepository dishRepository) {


        // Создаем категории
        Category pizza = new Category();
        pizza.setName("Пицца");
        pizza.setDescription("Итальянские пиццы на тонком тесте");
        categoryRepository.save(pizza);

        Category pasta = new Category();
        pasta.setName("Паста");
        pasta.setDescription("Разнообразные виды пасты");
        categoryRepository.save(pasta);

        Category salad = new Category();
        salad.setName("Салаты");
        salad.setDescription("Свежие салаты");
        categoryRepository.save(salad);

        Category drinks = new Category();
        drinks.setName("Напитки");
        drinks.setDescription("Прохладительные напитки");
        categoryRepository.save(drinks);

        // Добавляем тестовые блюда с imageUrl
        Dish margherita = new Dish();
        margherita.setName("Пицца Маргарита");
        margherita.setCategory("Пицца");
        margherita.setCategoryEntity(pizza);
        margherita.setPrice(550);
        margherita.setCookTime(15);
        margherita.setImageUrl("/images/dishes/marg.jpg"); // Пример пути
        dishRepository.save(margherita);

        Dish pepperoni = new Dish();
        pepperoni.setName("Пицца Пепперони");
        pepperoni.setCategory("Пицца");
        pepperoni.setCategoryEntity(pizza);
        pepperoni.setPrice(650);
        pepperoni.setCookTime(15);
        pepperoni.setImageUrl("/images/dishes/pep.jpg");
        dishRepository.save(pepperoni);

        Dish carbonara = new Dish();
        carbonara.setName("Карбонара");
        carbonara.setCategory("Паста");
        carbonara.setCategoryEntity(pasta);
        carbonara.setPrice(450);
        carbonara.setCookTime(12);
        carbonara.setImageUrl("/images/dishes/carb.jpg");
        dishRepository.save(carbonara);

        Dish caesar = new Dish();
        caesar.setName("Цезарь");
        caesar.setCategory("Салаты");
        caesar.setCategoryEntity(salad);
        caesar.setPrice(380);
        caesar.setCookTime(5);
        caesar.setImageUrl("/images/dishes/salad.jpg");
        dishRepository.save(caesar);

        Dish cola = new Dish();
        cola.setName("Кола");
        cola.setCategory("Напитки");
        cola.setCategoryEntity(drinks);
        cola.setPrice(120);
        cola.setCookTime(1);
        cola.setImageUrl("/images/dishes/cola.jpg");
        dishRepository.save(cola);

        System.out.println("Тестовое меню создано успешно!");
    }
}