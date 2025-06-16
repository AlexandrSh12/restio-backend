package com.restio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.restio.model.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        logger.info("Processing request to: " + request.getRequestURI());
        logger.info("Auth header: " + authHeader);

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);

                // Сначала проверим валидность токена
                if (jwtUtil.validateToken(jwt)) {
                    username = jwtUtil.getUsernameFromToken(jwt);
                    logger.info("Extracted username from token: " + username);
                } else {
                    logger.warning("Invalid JWT token");
                }
            }
        } catch (Exception e) {
            logger.warning("JWT token parsing failed: " + e.getMessage());
            jwt = null;
            username = null;
        }

        // Если пользователь извлечен из токена и контекст безопасности пуст
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Processing authentication for user: " + username);
            logger.info("Current SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());
            try {
                // Загружаем данные пользователя из базы
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Дополнительная проверка токена с данными пользователя
                if (jwtUtil.validateToken(jwt, userDetails)) {

                    // Извлекаем роли из токена
                    Set<Role> rolesFromToken = jwtUtil.getRolesFromToken(jwt);

                    // Преобразуем роли в GrantedAuthority с префиксом ROLE_
                    Collection<GrantedAuthority> authorities = rolesFromToken.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toList());

                    // Создаем токен аутентификации с ролями из JWT
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Устанавливаем аутентификацию в контекст безопасности
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    logger.info("Authentication successful for user: " + username +
                            " with roles: " + authorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(", ")));
                } else {
                    logger.warning("Token validation failed for user: " + username);
                }
            } catch (Exception e) {
                logger.warning("Authentication failed for user " + username + ": " + e.getMessage());
                // Очищаем контекст безопасности в случае ошибки
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}