package com.level_up_gamer.BackEnd.DTO.Orden;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuesta de items en una orden de compra
 * Usado en OrdenResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Item individual en una orden de compra")
public class OrdenItemResponse {
    
    @Schema(description = "ID del producto", example = "5")
    private Long productoId;
    
    @Schema(description = "Nombre del producto", example = "PlayStation 5")
    private String productoNombre;
    
    @Schema(description = "Cantidad comprada", example = "2")
    private Integer cantidad;
    
    @Schema(description = "Precio unitario en el momento de la compra", example = "450000")
    private Integer precioUnitario;
    
    @Schema(description = "Subtotal del item (precio * cantidad)", example = "900000")
    private Integer subtotal;
}
