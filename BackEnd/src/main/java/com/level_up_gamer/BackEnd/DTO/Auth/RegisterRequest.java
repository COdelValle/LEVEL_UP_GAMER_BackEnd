package com.level_up_gamer.BackEnd.DTO.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.level_up_gamer.BackEnd.Validation.RUT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para registro de nuevos usuarios
 * 
 * Usado en: POST /api/auth/register
 * 
 * Validaciones:
 * - nombre: Entre 2 y 100 caracteres, no puede estar vacío
 * - email: Formato válido, no puede estar vacío, debe ser único
 * - password: Entre 6 y 100 caracteres, no puede estar vacío
 * - passwordConfirm: Debe coincidir exactamente con password
 * 
 * @author LEVEL UP GAMER Development Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud de registro de nuevo usuario")
public class RegisterRequest {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    @Schema(description = "Correo electrónico único para el usuario", example = "usuario@gmail.com")
    private String email;
    
    @NotBlank(message = "El RUT es requerido")
    @RUT(message = "El RUT no es válido")
    @Schema(description = "RUT chileno del usuario (sin puntos, con guión opcional)", example = "12345678-5")
    private String rut;
    
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Schema(description = "Contraseña segura (mínimo 6 caracteres)", example = "MiPassword123")
    private String password;
    
    @NotBlank(message = "La confirmación de contraseña es requerida")
    @Schema(description = "Confirmación de la contraseña (debe ser idéntica)", example = "MiPassword123")
    private String passwordConfirm;
}
