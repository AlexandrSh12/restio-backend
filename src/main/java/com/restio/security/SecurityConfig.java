package com.restio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Включаем аннотации безопасности на методах
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF для простоты, в продакшене следует включить
                .cors(withDefaults()) // Используем настройки CORS из WebConfig
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll() // Доступ к консоли H2
                        .requestMatchers("/api/public/**").permitAll() // Публичные API (если есть)
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN") // Доступ только для админов
                        .requestMatchers("/api/chef/**").hasAuthority("ROLE_CHEF") // Доступ только для поваров
                        .requestMatchers("/api/waiter/**").hasAuthority("ROLE_WAITER") // Доступ только для официантов
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .userDetailsService(userDetailsService) // Устанавливаем наш UserDetailsService
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Для работы с H2 консолью
                )
                .httpBasic(withDefaults()); // Используем базовую HTTP-аутентификацию

        return http.build();
    }
}