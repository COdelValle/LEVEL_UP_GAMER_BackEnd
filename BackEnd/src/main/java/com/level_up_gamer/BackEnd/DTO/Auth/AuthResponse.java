package com.level_up_gamer.BackEnd.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuesta de autenticación exitosa
 * 
 * Usado en: Respuesta de POST /api/auth/login y POST /api/auth/register
 * 
 * Contiene:
 * - token: JWT para autenticación en peticiones posteriores (válido 24h)
 * - apiKey: Clave para autenticación máquina-máquina
 * - usuarioId: ID del usuario registrado
 * - nombre, email, rol: Información del usuario
 * 
 * @author LEVEL UP GAMER Development Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta exitosa después de autenticarse o registrarse")
public class AuthResponse {
    
    @Schema(
        description = "JWT Token para autenticación en peticiones futuras. Enviar en header: Authorization: Bearer {token}",
        example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvQG1haWwuY29tIiwiZW1wbGVhZG9JZCI6MSwiZXhwIjoxNzAwMDAwMDAwfQ..."
    )
    private String token;
    
    @Schema(
        description = "API Key para autenticación alternativa (máquina a máquina). Enviar en header: X-API-Key: {apiKey}",
        example = "Ym9nYXJhZGlvbjpjb2RlVmFsbGU6bGV2ZWxVcEdhbWVy..."
    )
    private String apiKey;
    
    @Schema(description = "Identificador único del usuario en la base de datos", example = "42")
    private Long usuarioId;
    
    @Schema(description = "Nombre completo del usuario registrado", example = "Juan Pérez")
    private String nombre;
    
    @Schema(description = "Correo electrónico del usuario", example = "usuario@gmail.com")
    private String email;
    
    @Schema(description = "Rol del usuario en el sistema", example = "USER")
    private String rol;
}
