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

   /* @Autowired
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
        waiter.setFullName("Иван Иванов");
        waiter.setRoles(Collections.singleton(Role.WAITER));
        userRepository.save(waiter);

        // Создаем повара
        User chef = new User();
        chef.setUsername("chef");
        chef.setPassword(passwordEncoder.encode("chef123"));
        chef.setFullName("Петр Петров");
        chef.setRoles(Collections.singleton(Role.CHEF));
        userRepository.save(chef);

        System.out.println("Тестовые пользователи созданы успешно!");
    }*/

    /*private void initMenu(CategoryRepository categoryRepository, DishRepository dishRepository) {


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
        margherita.setImageUrl("https://sun9-39.userapi.com/impg/oW5ALbk8jx83-lf1SxF6aV_pbYmDiFvHljiRcw/nM5vXKVx0Co.jpg?size=624x416&quality=95&sign=56ac22b9308a7ec8794037cf667c3e36&type=album");
        dishRepository.save(margherita);

        Dish pepperoni = new Dish();
        pepperoni.setName("Пицца Пепперони");
        pepperoni.setCategory("Пицца");
        pepperoni.setCategoryEntity(pizza);
        pepperoni.setPrice(650);
        pepperoni.setCookTime(15);
        pepperoni.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/9/91/Pizza-3007395.jpg");
        dishRepository.save(pepperoni);

        Dish carbonara = new Dish();
        carbonara.setName("Карбонара");
        carbonara.setCategory("Паста");
        carbonara.setCategoryEntity(pasta);
        carbonara.setPrice(450);
        carbonara.setCookTime(12);
        carbonara.setImageUrl("https://sun9-79.userapi.com/impg/YnJrk2SyGZdOecX64quz-mr_pMxZXBJOhd0J3w/TVBuvv8nObo.jpg?size=2500x1250&quality=95&sign=b8c1b25c101b59cdbac8b811950ee8bb&type=album");
        dishRepository.save(carbonara);

        Dish caesar = new Dish();
        caesar.setName("Цезарь");
        caesar.setCategory("Салаты");
        caesar.setCategoryEntity(salad);
        caesar.setPrice(380);
        caesar.setCookTime(5);
        caesar.setImageUrl("https://sun9-80.userapi.com/impg/NJidqhFjfeXPf35KOC4o1Jq0JO_5dcn3HNewTw/-vGguE9eVlI.jpg?size=1342x930&quality=95&sign=4f242e32e049273e23e09b5ad31e4b98&type=album");
        dishRepository.save(caesar);

        Dish cola = new Dish();
        cola.setName("Кола");
        cola.setCategory("Напитки");
        cola.setCategoryEntity(drinks);
        cola.setPrice(120);
        cola.setCookTime(1);
        cola.setImageUrl("https://sun9-3.userapi.com/impg/ruHFRuj7RL5_INEEip1ttora4MJmrsOjJBSkDg/jgGRWzpLxZ0.jpg?size=1024x1024&quality=95&sign=3c62f2389a48e1a9c57d14f832caaf09&type=album");
        dishRepository.save(cola);

        System.out.println("Тестовое меню создано успешно!");
    }*/
}