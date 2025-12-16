package com.level_up_gamer.BackEnd.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.level_up_gamer.BackEnd.DTO.Auth.AuthResponse;
import com.level_up_gamer.BackEnd.DTO.Auth.LoginRequest;
import com.level_up_gamer.BackEnd.DTO.Auth.RegisterRequest;
import com.level_up_gamer.BackEnd.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador de Autenticación
 * 
 * Proporciona endpoints para:
 * - Registro de nuevos usuarios
 * - Login/Autenticación
 * - Validación de tokens JWT
 * - Renovación de tokens
 * 
 * @author LEVEL UP GAMER Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Autenticación", description = "API para registro, login y gestión de autenticación")
public class AuthController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Registra un nuevo usuario en el sistema
     * 
     * Validaciones:
     * - Nombre: Requerido, 2-100 caracteres
     * - Email: Requerido, formato válido, debe ser único
     * - Contraseña: Requerida, 6-100 caracteres
     * - Confirmación: Debe coincidir exactamente con contraseña
     * 
     * Proceso:
     * 1. Valida datos de entrada
     * 2. Cifra la contraseña con BCrypt
     * 3. Genera API Key
     * 4. Crea JWT Token
     * 
     * @param request Datos del nuevo usuario
     * @return AuthResponse con token y datos del usuario
     */
    @PostMapping("/register")
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una nueva cuenta de usuario. La contraseña se cifra de forma segura " +
                    "y se genera un JWT Token para autenticación inmediata.",
            tags = {"Autenticación"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente. Se retorna token JWT, API Key y datos del usuario.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación:\n" +
                            "- Email ya está registrado\n" +
                            "- Nombre vacío o muy corto (mínimo 2 caracteres)\n" +
                            "- Contraseña muy corta (mínimo 6 caracteres)\n" +
                            "- Contraseñas no coinciden\n" +
                            "- Email inválido o mal formado",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Error de procesamiento: datos inválidos que no pueden procesarse",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor: fallo al encriptar contraseña o generar token",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = usuarioService.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Autentica un usuario y retorna JWT Token
     * 
     * Proceso de login:
     * 1. Busca usuario por email
     * 2. Verifica contraseña con BCrypt
     * 3. Si es válido, genera JWT Token
     * 4. Retorna token, API Key e información del usuario
     * 
     * @param request Email y contraseña
     * @return AuthResponse con token o error 401
     */
    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuario",
            description = "Valida credenciales y retorna un JWT Token válido por 24 horas. " +
                    "El token debe enviarse en el header Authorization: Bearer {token}",
            tags = {"Autenticación"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa. Se retorna JWT Token (válido 24h), API Key y datos del usuario.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales rechazadas:\n" +
                            "- Email no registrado en el sistema\n" +
                            "- Contraseña incorrecta\n" +
                            "- Usuario inactivo o bloqueado",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación:\n" +
                            "- Email vacío o inválido\n" +
                            "- Contraseña vacía\n" +
                            "- Formato de solicitud incorrecto",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno: fallo en validación de contraseña o generación de token",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = usuarioService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Valida un JWT Token sin hacer logout
     * 
     * Útil para:
     * - Verificar si el token es válido antes de hacer una petición
     * - Refrescar el estado de autenticación
     * - Detectar tokens expirados
     * 
     * @param token JWT Token en header Authorization
     * @return ValidateTokenResponse con resultado
     */
    @PostMapping("/validate")
    @Operation(
            summary = "Validar JWT Token",
            description = "Verifica si un JWT Token es válido sin consumir su ciclo de vida. " +
                    "Retorna 200 si es válido o 401 si está expirado o corrupto.",
            tags = {"Autenticación"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token validado exitosamente. Campo 'valid' será true y message contiene confirmación.",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token inválido o rechazado:\n" +
                            "- Token expirado (mayor a 24 horas)\n" +
                            "- Firma JWT corrupta o inválida\n" +
                            "- Token malformado\n" +
                            "- Token de usuario inexistente o eliminado",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en solicitud:\n" +
                            "- Header Authorization faltante\n" +
                            "- Formato incorrecto del header",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno: fallo al validar token en servidor",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Remover "Bearer " del inicio si existe
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            
            if (usuarioService.validarToken(jwtToken)) {
                return ResponseEntity.ok(new ValidateTokenResponse(true, "Token válido"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ValidateTokenResponse(false, "Token inválido"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ValidateTokenResponse(false, e.getMessage()));
        }
    }
    
    /**
     * Clase interna para respuesta de error
     */
    public static class ErrorResponse {
        public String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() {
            return error;
        }
    }
    
    /**
     * Clase interna para respuesta de validación de token
     */
    public static class ValidateTokenResponse {
        public boolean valid;
        public String message;
        
        public ValidateTokenResponse(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
