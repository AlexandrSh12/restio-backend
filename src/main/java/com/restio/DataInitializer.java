package com.restio;

import com.restio.model.Category;
import com.restio.model.Dish;
import com.restio.repository.CategoryRepository;
import com.restio.repository.DishRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepository, DishRepository dishRepository) {
        return args -> {
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

            // Добавляем тестовые блюда
            Dish margherita = new Dish();
            margherita.setName("Пицца Маргарита");
            margherita.setCategory("Пицца");
            margherita.setPrice(550);
            margherita.setCookTime(15);
            dishRepository.save(margherita);

            Dish pepperoni = new Dish();
            pepperoni.setName("Пицца Пепперони");
            pepperoni.setCategory("Пицца");
            pepperoni.setPrice(650);
            pepperoni.setCookTime(15);
            dishRepository.save(pepperoni);

            Dish carbonara = new Dish();
            carbonara.setName("Карбонара");
            carbonara.setCategory("Паста");
            carbonara.setPrice(450);
            carbonara.setCookTime(12);
            dishRepository.save(carbonara);

            Dish caesar = new Dish();
            caesar.setName("Цезарь");
            caesar.setCategory("Салаты");
            caesar.setPrice(380);
            caesar.setCookTime(5);
            dishRepository.save(caesar);

            Dish cola = new Dish();
            cola.setName("Кола");
            cola.setCategory("Напитки");
            cola.setPrice(120);
            cola.setCookTime(1);
            dishRepository.save(cola);
        };
    }
}