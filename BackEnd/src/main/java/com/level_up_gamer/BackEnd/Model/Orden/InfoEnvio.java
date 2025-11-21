package com.level_up_gamer.BackEnd.Model.Orden;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InfoEnvio {
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = true)
    private String apellido;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String telefono;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column(nullable = true)
    private String departamento;
    
    @Column(nullable = false)
    private String ciudad;
    
    @Column(nullable = false)
    private String region;
    
    @Column(nullable = false)
    private String comuna;
    
    @Column(nullable = true)
    private String codigoPostal;
    
    @Column(nullable = false)
    private String pais;
}
