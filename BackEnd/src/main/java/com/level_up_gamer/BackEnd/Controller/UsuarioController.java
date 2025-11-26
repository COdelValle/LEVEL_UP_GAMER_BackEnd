package com.level_up_gamer.BackEnd.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
import com.level_up_gamer.BackEnd.DTO.Usuario.UpdateUsuarioRequest;
import com.level_up_gamer.BackEnd.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gestionar Usuarios
 * 
 * @author LEVEL UP GAMER Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    // Obtener info:
        // Todos los Usuarios:
        @GetMapping
        @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados")
        public ResponseEntity<List<Usuario>> getAllUsuarios() {
            List<Usuario> usuarios = usuarioService.getUsuarios();
            return ResponseEntity.ok(usuarios);
        }
        
        // Por ID:
        @GetMapping("/{id}")
        @Operation(summary = "Obtener usuario por ID", description = "Retorna los detalles de un usuario específico")
        public ResponseEntity<?> getUsuarioById(@PathVariable Long id) {
            Usuario usuario = usuarioService.getUsuarioByID(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado"));
            }
            return ResponseEntity.ok(usuario);
        }
        
        // Por email:
        @GetMapping("/email/{email}")
        @Operation(summary = "Obtener usuario por email", description = "Retorna los detalles de un usuario buscando por email")
        public ResponseEntity<?> getUsuarioByEmail(@PathVariable String email) {
            Usuario usuario = usuarioService.getUsuarioByEmail(email);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado"));
            }
            return ResponseEntity.ok(usuario);
        }
    
    // Actualizar usuario:
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario (nombre, email, contraseña, rol)")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @Valid @RequestBody UpdateUsuarioRequest request) {
        try {
            Usuario usuario = usuarioService.getUsuarioByID(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado"));
            }
            
            // Actualizar nombre si se proporciona
            if (request.getNombre() != null && !request.getNombre().isEmpty()) {
                usuario.setNombre(request.getNombre());
            }
            
            // Actualizar email si se proporciona
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                // Verificar que el nuevo email no esté en uso
                Usuario usuarioExistente = usuarioService.getUsuarioByEmail(request.getEmail());
                if (usuarioExistente != null && !usuarioExistente.getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("El email ya está registrado por otro usuario"));
                }
                usuario.setEmail(request.getEmail());
            }
            
            // Actualizar contraseña si se proporciona
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                // Validar que passwordConfirm coincida
                if (request.getPasswordConfirm() == null || !request.getPassword().equals(request.getPasswordConfirm())) {
                    return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Las contraseñas no coinciden"));
                }
                
                // Validar longitud mínima de contraseña
                if (request.getPassword().length() < 6) {
                    return ResponseEntity.badRequest()
                        .body(new ErrorResponse("La contraseña debe tener al menos 6 caracteres"));
                }
                
                // Encriptar y actualizar contraseña
                String passwordEncriptada = usuarioService.encryptPassword(request.getPassword());
                usuario.setPassword(passwordEncriptada);
            }
            
            // Actualizar rol si se proporciona
            if (request.getRol() != null && !request.getRol().isEmpty()) {
                try {
                    usuario.setRol(com.level_up_gamer.BackEnd.Model.Usuario.RolUsuario.valueOf(request.getRol().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Rol inválido. Valores permitidos: USER, ADMIN, SELLER, GUEST"));
                }
            }
            
            Usuario usuarioActualizado = usuarioService.saveUsuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // Eliminar Usuario:
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        Boolean deleted = usuarioService.deleteUsuarioById(id);
        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario eliminado correctamente");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Usuario no encontrado"));
        }
    }
    
    // Clase interna para manejar errores:
    public static class ErrorResponse {
        public String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() {
            return error;
        }
    }
}
