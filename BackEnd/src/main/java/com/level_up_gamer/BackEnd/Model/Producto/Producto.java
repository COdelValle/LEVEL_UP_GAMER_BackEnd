package com.level_up_gamer.BackEnd.Model.Producto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "producto")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(nullable = false)
    private Integer precio;
    
    @Column(nullable = false)
    private String categoria;
    
    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    private String imagen;
    
    @Column(columnDefinition = "VARCHAR(1000)")
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
    
    @Lob
    @Column(columnDefinition = "CLOB CHECK (especificaciones IS JSON)")
    private String especificaciones;
}