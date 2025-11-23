package com.level_up_gamer.BackEnd.DTO.Orden;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para item de una orden de compra
 * Usado en CreateOrdenRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Item individual en una orden de compra")
public class OrdenItemRequest {
    
    @NotNull(message = "El ID del producto es requerido")
    @Positive(message = "El ID del producto debe ser positivo")
    @Schema(description = "ID único del producto a comprar", example = "5")
    private Long productoId;
    
    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a 0")
    @Max(value = 1000, message = "La cantidad no puede exceder 1000 unidades")
    @Schema(description = "Cantidad de unidades a comprar", example = "2")
    private Integer cantidad;
    
    @NotNull(message = "El precio es requerido")
    @Positive(message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio unitario en el momento de la compra", example = "450000")
    private Integer precio;
}
