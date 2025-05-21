package com.restio.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    // Constructor removed to break circular dependency

    // PasswordEncoder bean moved to PasswordConfig.java

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabling CSRF for simplicity, enable in production
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/public/**").permitAll() // Example: public endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Example: admin endpoints
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .userDetailsService(userDetailsService) // Set the custom UserDetailsService
                .httpBasic(withDefaults()); // Use HTTP Basic authentication
        return http.build();
    }
}
