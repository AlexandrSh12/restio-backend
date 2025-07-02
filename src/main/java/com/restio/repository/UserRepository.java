// UserRepository.java
package com.restio.repository;

import com.restio.model.User;
import com.restio.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Существующие методы
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    // Дополнительные полезные методы

    // Поиск активных пользователей
    List<User> findByActiveTrue();

    // Поиск пользователей по роли
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role AND u.active = true")
    List<User> findActiveUsersByRole(@Param("role") Role role);

    // Найти всех официантов
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = 'WAITER' AND u.active = true")
    List<User> findActiveWaiters();

    // Найти всех поваров
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = 'CHEF' AND u.active = true")
    List<User> findActiveChefs();

    // Поиск по имени пользователя (для админки)
    List<User> findByUsernameContainingIgnoreCase(String username);

    // Поиск по полному имени
    List<User> findByFullNameContainingIgnoreCase(String fullName);

    // Проверить, есть ли хотя бы один админ
    @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.roles r WHERE r = 'ADMIN' AND u.active = true")
    boolean hasActiveAdmin();
}