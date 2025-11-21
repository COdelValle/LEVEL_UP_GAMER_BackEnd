package com.level_up_gamer.BackEnd.Model.Producto;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categoria")
public class CategoriaProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(columnDefinition = "VARCHAR2(500)")
    private String descripcion;
    
    @Column(nullable = true)
    private String icono;
    
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList<>();
}
