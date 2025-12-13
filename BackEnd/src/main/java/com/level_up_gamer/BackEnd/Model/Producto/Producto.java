package com.level_up_gamer.BackEnd.Model.Producto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String nombre;
    
    @Column(nullable = false)
    @NotNull(message = "El precio es requerido")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private Integer precio;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "La categoría es requerida")
    private CategoriaProducto categoria;
    
    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    @NotBlank(message = "La imagen es requerida")
    private String imagen;
    
    @Column(columnDefinition = "VARCHAR(1000)")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @Column(nullable = false)
    @NotNull(message = "El stock es requerido")
    @Min(value = 0, message = "El stock no puede ser negativo")
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