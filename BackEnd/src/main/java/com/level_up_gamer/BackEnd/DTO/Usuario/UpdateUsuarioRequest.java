package com.level_up_gamer.BackEnd.DTO.Usuario;

import jakarta.validation.constraints.*;
import com.level_up_gamer.BackEnd.Validation.RUT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para actualizar información del usuario
 * Usado en: PUT /api/usuarios/{id}
 * 
 * Campos opcionales:
 * - nombre: actualizar nombre completo
 * - email: cambiar email (debe ser único)
 * - password: cambiar contraseña (requiere passwordConfirm)
 * - passwordConfirm: confirmación de nueva contraseña
 * - rol: cambiar rol (solo ADMIN puede hacer esto)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para actualizar información del usuario")
public class UpdateUsuarioRequest {
    
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre completo del usuario (opcional)", example = "Juan Pérez")
    private String nombre;
    
    @Email(message = "El email debe ser válido")
    @Schema(description = "Correo electrónico nuevo (debe ser único y válido)", example = "nuevo@example.com")
    private String email;
    
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Schema(description = "Nueva contraseña (mínimo 6 caracteres). Si se proporciona, passwordConfirm es obligatorio", example = "NuevaPassword123!")
    private String password;
    
    @Schema(description = "Confirmación de la nueva contraseña (debe coincidir exactamente con password)", example = "NuevaPassword123!")
    private String passwordConfirm;
    
    @Schema(description = "Rol del usuario en el sistema. Solo ADMIN puede cambiar roles. Valores: USER, ADMIN, SELLER, GUEST", example = "ADMIN")
    private String rol;

    @RUT(message = "El RUT no es válido")
    @Schema(description = "RUT chileno del usuario (opcional)", example = "12345678-5")
    private String rut;
}
