package com.level_up_gamer.BackEnd.Model.Usuario;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password; // Usar BCryptPasswordEncoder
    
    @Column(nullable = true)
    private Boolean duoc = false;
    
    @Column(nullable = true)
    private Integer puntos = 0;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolUsuario rol = RolUsuario.USER;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(nullable = true)
    private LocalDateTime ultimoAcceso;
}
