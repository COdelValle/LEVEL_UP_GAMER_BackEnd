package com.level_up_gamer.BackEnd.DTO.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para autenticación de usuarios
 * 
 * Usado en: POST /api/auth/login
 * 
 * Validaciones:
 * - email: Formato válido de email, no puede estar vacío
 * - password: Entre 6 y 100 caracteres, no puede estar vacío
 * 
 * @author LEVEL UP GAMER Development Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud de autenticación con credenciales de usuario")
public class LoginRequest {
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @Schema(description = "Correo electrónico registrado", example = "usuario@gmail.com")
    private String email;
    
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario", example = "MiPassword123")
    private String password;
}
