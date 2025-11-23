package com.level_up_gamer.BackEnd.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductoRequest {
    
    @NotBlank(message = "El nombre del producto es requerido")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombre;
    
    @NotNull(message = "El precio es requerido")
    @Min(value = 0, message = "El precio debe ser mayor a 0")
    private Integer precio;
    
    @NotBlank(message = "La categoría es requerida")
    private String categoria;
    
    @NotBlank(message = "La imagen es requerida")
    private String imagen;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @NotNull(message = "El stock es requerido")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    private Boolean destacado = false;
    private Boolean nuevo = false;
    private Boolean oferta = false;
    
    @Min(value = 0, message = "El precio de oferta debe ser mayor a 0")
    private Integer precioOferta;
    
    private String especificaciones;
}
