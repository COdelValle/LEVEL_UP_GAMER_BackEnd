package com.level_up_gamer.BackEnd.Model.Producto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Integer precio;
    
    @Column(nullable = false)
    private String categoria;
    
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String imagen;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(nullable = false)
    private Boolean destacado = false;
    
    @Column(nullable = false)
    private Boolean nuevo = false;
    
    @Column(nullable = true)
    private Boolean oferta = false;
    
    @Column(nullable = true)
    private Integer precioOferta;
    
    @Column(columnDefinition = "JSON")
    private String especificaciones;
}