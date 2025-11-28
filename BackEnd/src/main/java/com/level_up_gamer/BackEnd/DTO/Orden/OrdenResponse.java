package com.level_up_gamer.BackEnd.DTO.Orden;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import com.level_up_gamer.BackEnd.Model.Orden.InfoEnvio;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuesta de orden de compra
 * Usado en respuestas de GET y POST de órdenes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información detallada de una orden de compra")
public class OrdenResponse {
    
    @Schema(description = "ID único de la orden", example = "42")
    private Long id;
    
    @Schema(description = "Número único de seguimiento", example = "ORD-2025-001")
    private String numero;
    
    @Schema(description = "Fecha y hora de creación", example = "2025-11-23T10:30:00")
    private LocalDateTime fecha;
    
    @Schema(description = "Total de la compra en pesos", example = "250000")
    private Integer total;
    
    @Schema(description = "Estado actual de la orden (PENDIENTE, PROCESADA, ENVIADA, ENTREGADA)", example = "PROCESADA")
    private String estado;
    
    @Schema(description = "Método de pago utilizado", example = "Tarjeta")
    private String metodoPago;
    
    @Schema(description = "ID del usuario que compró", example = "1")
    private Long usuarioId;
    
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String usuarioNombre;
    
    @Schema(description = "Información de envío")
    private InfoEnvio infoEnvio;
    
    @Schema(description = "Items comprados en esta orden")
    private List<OrdenItemResponse> items;
}
