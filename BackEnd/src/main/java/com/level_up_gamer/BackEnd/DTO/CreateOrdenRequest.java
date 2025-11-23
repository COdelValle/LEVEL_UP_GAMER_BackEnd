package com.level_up_gamer.BackEnd.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrdenRequest {
    
    @NotEmpty(message = "La orden debe tener al menos un artículo")
    private List<OrdenItemRequest> items;
    
    @NotNull(message = "El método de pago es requerido")
    private String metodoPago;
    
    @NotBlank(message = "El nombre de envío es requerido")
    private String nombreEnvio;
    
    @NotBlank(message = "El teléfono de envío es requerido")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos")
    private String telefonoEnvio;
    
    @NotBlank(message = "La dirección de envío es requerida")
    @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres")
    private String direccionEnvio;
    
    @NotBlank(message = "La ciudad de envío es requerida")
    private String ciudadEnvio;
    
    @NotBlank(message = "La región de envío es requerida")
    private String regionEnvio;
    
    @NotBlank(message = "El código postal es requerido")
    @Pattern(regexp = "^[0-9]{6,8}$", message = "El código postal debe tener entre 6 y 8 dígitos")
    private String codigoPostal;
    
    private String observaciones;
}
