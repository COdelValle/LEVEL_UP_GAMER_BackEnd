package com.level_up_gamer.BackEnd.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuesta de información del usuario
 * Usado en respuestas de GET de usuarios
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información del usuario")
public class UsuarioResponse {
    
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo", example = "Juan Pérez")
    private String nombre;
    
    @Schema(description = "Correo electrónico", example = "usuario@gmail.com")
    private String email;
    
    @Schema(description = "Teléfono de contacto")
    private String telefono;
    
    @Schema(description = "Dirección residencial")
    private String direccion;
    
    @Schema(description = "Rol en el sistema", example = "USER")
    private String rol;
}
