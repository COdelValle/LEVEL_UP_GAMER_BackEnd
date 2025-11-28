package com.level_up_gamer.BackEnd.DTO.Orden;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import com.level_up_gamer.BackEnd.Model.Orden.EstadoOrden;

/**
 * DTO para actualizar una orden existente
 * Usado en: PUT /api/v1/ordenes/{id}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para actualizar el estado de una orden")
public class UpdateOrdenRequest {
    
    @NotNull(message = "El estado es requerido")
    @Schema(description = "Nuevo estado de la orden", example = "COMPLETADO")
    private EstadoOrden estado;
    
    @Schema(description = "Observaciones adicionales (opcional)", example = "Enviado exitosamente")
    private String observaciones;
}
