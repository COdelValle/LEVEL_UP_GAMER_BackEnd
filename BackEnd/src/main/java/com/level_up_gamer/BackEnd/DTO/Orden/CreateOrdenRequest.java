package com.level_up_gamer.BackEnd.DTO.Orden;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * DTO para crear una nueva orden de compra
 * Usado en: POST /api/ordenes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para crear una nueva orden de compra")
public class CreateOrdenRequest {
    
    @NotEmpty(message = "La orden debe tener al menos un artículo")
    @Schema(description = "Lista de items a comprar")
    private List<OrdenItemRequest> items;
    
    @NotNull(message = "El método de pago es requerido")
    @Schema(description = "Método de pago (Tarjeta, PayPal, Transferencia)", example = "Tarjeta")
    private String metodoPago;
    
    @NotBlank(message = "El nombre de envío es requerido")
    @Schema(description = "Nombre completo para la entrega", example = "Juan Pérez")
    private String nombreEnvio;
    
    @NotBlank(message = "El teléfono de envío es requerido")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos")
    @Schema(description = "Teléfono de contacto", example = "912345678")
    private String telefonoEnvio;
    
    @NotBlank(message = "La dirección de envío es requerida")
    @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres")
    @Schema(description = "Dirección de entrega completa", example = "Calle Principal 123, Apto 4B")
    private String direccionEnvio;
    
    @NotBlank(message = "La ciudad de envío es requerida")
    @Schema(description = "Ciudad de entrega", example = "Santiago")
    private String ciudadEnvio;
    
    @NotBlank(message = "La región de envío es requerida")
    @Schema(description = "Región o estado", example = "Metropolitana")
    private String regionEnvio;
    
    @NotBlank(message = "El código postal es requerido")
    @Pattern(regexp = "^[0-9]{6,8}$", message = "El código postal debe tener entre 6 y 8 dígitos")
    @Schema(description = "Código postal", example = "8330000")
    private String codigoPostal;
    
    @Schema(description = "Observaciones adicionales para la entrega", example = "Entregar de lunes a viernes")
    private String observaciones;
}
