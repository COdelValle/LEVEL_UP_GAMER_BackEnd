package com.level_up_gamer.BackEnd.Model.Orden;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InfoEnvio {
    
    @Column(nullable = false)
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    
    @Column(nullable = true)
    private String apellido;
    
    @Column(nullable = false)
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @Column(nullable = false)
    @NotBlank(message = "El teléfono es requerido")
    private String telefono;
    
    @Column(nullable = false)
    @NotBlank(message = "La dirección es requerida")
    private String direccion;
    
    @Column(nullable = true)
    private String departamento;
    
    @Column(nullable = false)
    @NotBlank(message = "La ciudad es requerida")
    private String ciudad;
    
    @Column(nullable = false)
    @NotBlank(message = "La región es requerida")
    private String region;
    
    @Column(nullable = false)
    @NotBlank(message = "La comuna es requerida")
    private String comuna;
    
    @Column(nullable = true)
    private String codigoPostal;
    
    @Column(nullable = false)
    @NotBlank(message = "El país es requerido")
    private String pais;
}
