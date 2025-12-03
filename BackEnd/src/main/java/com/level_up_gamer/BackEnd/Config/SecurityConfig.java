package com.level_up_gamer.BackEnd.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.level_up_gamer.BackEnd.Security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                    // Endpoints públicos de autenticación
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    // Documentación y Swagger
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v1/api-docs/**",
                        "/api-docs/**",
                        "/docs",
                        "/docs/**"
                    ).permitAll()
                    // Productos: GET público (listar y ver por ID), POST requiere autenticación
                    .requestMatchers("GET", "/api/v1/productos").permitAll()
                    .requestMatchers("GET", "/api/v1/productos/**").permitAll()
                    // Blog: GET público (listar, ver por ID, destacados, por autor), POST requiere autenticación
                    .requestMatchers("GET", "/api/v1/blog").permitAll()
                    .requestMatchers("GET", "/api/v1/blog/**").permitAll()
                    // Órdenes: POST público para crear compras
                    .requestMatchers("POST", "/api/v1/ordenes").permitAll()
                    // Usuarios: bulk creation requiere ADMIN
                    .requestMatchers("POST", "/api/v1/usuarios/bulk").hasRole("ADMIN")
                    // El resto requiere autenticación
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
