package com.restio.controller;

import com.restio.dto.UserDTO;
import com.restio.model.User;
import com.restio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Получаем пользователя из базы данных
        User user = userService.getUserByUsername(username);

        // Преобразуем в DTO для безопасной передачи клиенту
        UserDTO userDTO = userService.convertToDTO(user);

        return ResponseEntity.ok(userDTO);
    }

}