package com.level_up_gamer.BackEnd.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
import com.level_up_gamer.BackEnd.DTO.*;
import com.level_up_gamer.BackEnd.Service.UsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    // Obtener info:
        // Todos los Usuarios:
        @GetMapping
        public ResponseEntity<List<Usuario>> getAllUsuarios() {
            List<Usuario> usuarios = usuarioService.getUsuarios();
            return ResponseEntity.ok(usuarios);
        }
        
        // Por ID:
        @GetMapping("/{id}")
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
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @Valid @RequestBody UpdateUsuarioRequest request) {
        try {
            Usuario usuario = usuarioService.getUsuarioByID(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado"));
            }
            
            if (request.getNombre() != null && !request.getNombre().isEmpty()) {
                usuario.setNombre(request.getNombre());
            }
            
            Usuario usuarioActualizado = usuarioService.saveUsuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // Eliminar Usuario:
    @DeleteMapping("/{id}")
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
