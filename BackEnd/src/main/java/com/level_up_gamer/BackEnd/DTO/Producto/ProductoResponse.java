package com.level_up_gamer.BackEnd.DTO.Producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuesta de producto en listados y detalles
 * 
 * Usado en:
 * - GET /api/productos (listado)
 * - GET /api/productos/{id} (detalles)
 * - POST /api/productos (creación)
 * - PUT /api/productos/{id} (actualización)
 * 
 * Contiene toda la información necesaria para mostrar
 * productos al cliente en el catálogo y detalles.
 * 
 * @author LEVEL UP GAMER Development Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información detallada de un producto")
public class ProductoResponse {
    
    @Schema(description = "ID único del producto", example = "1")
    private Long id;
    
    @Schema(description = "Nombre del producto", example = "Nvidia RTX 4080")
    private String nombre;
    
    @Schema(description = "Precio regular en pesos", example = "450000")
    private Integer precio;
    
    @Schema(description = "Categoría del producto", example = "Gaming")
    private String categoria;
    
    @Schema(description = "URL de la imagen", example = "https://cdn.example.com/gpu/4080.jpg")
    private String imagen;
    
    @Schema(description = "Descripción del producto", example = "Tarjeta gráfica potente")
    private String descripcion;
    
    @Schema(description = "Stock disponible", example = "15")
    private Integer stock;
    
    @Schema(description = "¿Está destacado en inicio?", example = "true")
    private Boolean destacado;
    
    @Schema(description = "¿Es producto nuevo?", example = "false")
    private Boolean nuevo;
    
    @Schema(description = "¿Está en oferta?", example = "false")
    private Boolean oferta;
    
    @Schema(description = "Precio en oferta (si aplica)", example = "380000")
    private Integer precioOferta;
    
    @Schema(description = "Especificaciones técnicas JSON", example = "{\"RAM\":\"16GB\"}")
    private String especificaciones;
}
