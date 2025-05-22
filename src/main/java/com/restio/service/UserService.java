package com.restio.service;

import com.restio.dto.UserDTO;
import com.restio.exception.BadRequestException;
import com.restio.exception.ResourceNotFoundException;
import com.restio.model.Role;
import com.restio.model.User;
import com.restio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        // Шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Устанавливаем роли по умолчанию, если не указаны
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Collections.singleton(Role.WAITER));
        }

        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        // Проверяем, не занято ли уже новое имя пользователя
        if (!user.getUsername().equals(userDetails.getUsername()) &&
                userRepository.existsByUsername(userDetails.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        user.setUsername(userDetails.getUsername());

        // Обновляем пароль только если он предоставлен
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        user.setFullName(userDetails.getFullName());

        // Обновляем роли, если они предоставлены
        if (userDetails.getRoles() != null && !userDetails.getRoles().isEmpty()) {
            user.setRoles(userDetails.getRoles());
        }

        user.setActive(userDetails.isActive());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // Преобразование в DTO для безопасной передачи данных клиенту
    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRoles().stream()
                .findFirst()
                .map(Enum::name)
                .orElse(""));
        return dto;
    }
}