package com.level_up_gamer.BackEnd.DTO.Producto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para creación y actualización de productos
 * Usado en POST y PUT de productos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para crear o actualizar un producto")
public class CreateProductoRequest {
    
    @NotBlank(message = "El nombre del producto es requerido")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    @Schema(description = "Nombre único del producto", example = "Nvidia RTX 4080")
    private String nombre;
    
    @NotNull(message = "El precio es requerido")
    @Min(value = 0, message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio en pesos (sin decimales)", example = "450000")
    private Integer precio;
    
    @NotBlank(message = "La categoría es requerida")
    @Schema(description = "Categoría del producto", example = "Gaming")
    private String categoria;
    
    @NotBlank(message = "La imagen es requerida")
    @Schema(description = "URL de la imagen del producto", example = "https://cdn.example.com/gpu/4080.jpg")
    private String imagen;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Schema(description = "Descripción breve del producto", example = "Tarjeta gráfica de última generación")
    private String descripcion;
    
    @NotNull(message = "El stock es requerido")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad disponible en inventario", example = "15")
    private Integer stock;
    
    @Schema(description = "¿Producto destacado en la portada?", example = "true")
    private Boolean destacado = false;
    
    @Schema(description = "¿Producto nuevo en el catálogo?", example = "false")
    private Boolean nuevo = false;
    
    @Schema(description = "¿Producto en oferta?", example = "false")
    private Boolean oferta = false;
    
    @Min(value = 0, message = "El precio de oferta debe ser mayor a 0")
    @Schema(description = "Precio durante la oferta (solo si oferta=true)", example = "380000")
    private Integer precioOferta;
    
    @Schema(description = "Especificaciones técnicas en formato JSON", example = "{\"RAM\":\"16GB\",\"Almacenamiento\":\"1TB SSD\"}")
    private String especificaciones;
}
