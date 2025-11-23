package com.level_up_gamer.BackEnd.DTO.Usuario;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para actualizar información del usuario
 * Usado en: PUT /api/usuarios/{id}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para actualizar información del usuario")
public class UpdateUsuarioRequest {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;
    
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String telefono;
    
    @Schema(description = "Dirección residencial", example = "Calle Principal 123, Apto 4B")
    private String direccion;
}
