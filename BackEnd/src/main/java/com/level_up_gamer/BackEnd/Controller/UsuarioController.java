package com.level_up_gamer.BackEnd.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
import com.level_up_gamer.BackEnd.Model.Usuario.RolUsuario;
import com.level_up_gamer.BackEnd.DTO.Usuario.UpdateUsuarioRequest;
import com.level_up_gamer.BackEnd.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados. Acceso: requiere autenticación (cualquier usuario autenticado).")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente. Retorna array con todos los usuarios registrados."),
            @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente en header Authorization."),
            @ApiResponse(responseCode = "403", description = "No autorizado. Usuario sin permisos suficientes para listar usuarios."),
            @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos o servidor")
        })
        public ResponseEntity<List<Usuario>> getAllUsuarios() {
            List<Usuario> usuarios = usuarioService.getUsuarios();
            return ResponseEntity.ok(usuarios);
        }
        
        // Por ID:
        @GetMapping("/{id}")
        @Operation(summary = "Obtener usuario por ID", description = "Retorna los detalles de un usuario específico. Acceso: requiere autenticación (cualquier usuario autenticado).")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado. Retorna objeto completo con todos sus datos."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado. El ID proporcionado no existe en el sistema."),
            @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
            @ApiResponse(responseCode = "400", description = "ID inválido. El parámetro id debe ser un número entero válido."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
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
        @Operation(summary = "Obtener usuario por email", description = "Retorna los detalles de un usuario buscando por email. Acceso: requiere autenticación (cualquier usuario autenticado).")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado por email. Retorna objeto completo con todos sus datos."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado. El email proporcionado no está registrado en el sistema."),
            @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
            @ApiResponse(responseCode = "400", description = "Email inválido o mal formado. Verifique el formato del email."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
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
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario (nombre, email, contraseña, rol). Acceso: requiere autenticación. Nota: cambiar el rol requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente. Retorna objeto usuario con los datos modificados."),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado. El ID proporcionado no existe."),
        @ApiResponse(responseCode = "409", description = "Conflicto. Email o RUT ya están registrados por otro usuario."),
        @ApiResponse(responseCode = "400", description = "Error de validación: Contraseñas no coinciden, Contraseña muy corta (mín 6), Rol inválido (USER/ADMIN/SELLER/GUEST)"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede cambiar roles. Usuario sin permisos para actualizar."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida en el formato de la solicitud."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos o encriptación de contraseña")
    })
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

            // Actualizar RUT si se proporciona
            if (request.getRut() != null && !request.getRut().isEmpty()) {
                Usuario usuarioExistentePorRut = usuarioService.getUsuarioByRut(request.getRut());
                if (usuarioExistentePorRut != null && !usuarioExistentePorRut.getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ErrorResponse("El RUT ya está registrado por otro usuario"));
                }
                usuario.setRut(request.getRut());
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
            
            // Actualizar rol si se proporciona (solo ADMIN puede cambiar roles)
            if (request.getRol() != null && !request.getRol().isEmpty()) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                boolean isAdmin = auth != null && auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                if (!isAdmin) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse("Acceso denegado: solo administradores pueden cambiar el rol"));
                }

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
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema. Acceso: requiere autenticación (cualquier usuario autenticado).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente. Retorna confirmación con mensaje de éxito."),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado. El ID proporcionado no existe en el sistema."),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Usuario sin permisos para eliminar."),
        @ApiResponse(responseCode = "400", description = "ID inválido. El parámetro id debe ser un número entero válido."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al eliminar usuario de base de datos")
    })
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

    /**
     * Crea múltiples usuarios a la vez
     * Acceso: Solo ADMIN
     */
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear múltiples usuarios", description = "Crea varios usuarios en una sola solicitud. Acceso: requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuarios creados exitosamente. Retorna lista con usuarios creados y cantidad total."),
        @ApiResponse(responseCode = "400", description = "Error de validación: Datos inválidos, email duplicado, RUT duplicado, contraseña muy corta"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede crear múltiples usuarios."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida en formato de solicitud."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al crear usuarios en base de datos")
    })
    public ResponseEntity<?> crearMultiples(@Valid @RequestBody List<CreateBulkUsuarioRequest> requests) {
        List<Usuario> usuarios = requests.stream().map(request -> {
            Usuario usuario = new Usuario();
            usuario.setNombre(request.getNombre());
            usuario.setEmail(request.getEmail());
            usuario.setRut(request.getRut());
            usuario.setPassword(usuarioService.encryptPassword(request.getPassword()));
            usuario.setApiKey(usuarioService.generateApiKey());
            usuario.setRol(RolUsuario.valueOf(request.getRol().toUpperCase()));
            usuario.setFechaCreacion(LocalDateTime.now());
            return usuario;
        }).collect(Collectors.toList());

        List<Usuario> usuariosGuardados = usuarioService.saveAllUsuarios(usuarios);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Usuarios creados exitosamente");
        response.put("cantidad", usuariosGuardados.size());
        response.put("usuarios", usuariosGuardados);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Crea un usuario personalizado sin token y apikey
     * Acceso: Solo ADMIN
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear usuario personalizado", description = "Crea un usuario con datos personalizados; el sistema generará automáticamente la apiKey. Acceso: requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente. Retorna objeto usuario con apiKey generada automáticamente."),
        @ApiResponse(responseCode = "400", description = "Error de validación: Contraseña faltante, muy corta (mín 6), email vacío, datos requeridos faltantes"),
        @ApiResponse(responseCode = "409", description = "Conflicto. Email o RUT ya están registrados en el sistema."),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede crear usuarios personalizados."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida en formato de solicitud."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en encriptación o almacenamiento de usuario")
    })
    public ResponseEntity<?> crearUsuarioPersonalizado(@Valid @RequestBody CreateCustomUsuarioRequest request) {
        try {
            // Validar email único
            if (usuarioService.getUsuarioByEmail(request.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("El email ya está registrado"));
            }

            // Validar RUT único si se proporciona
            if (request.getRut() != null && !request.getRut().isEmpty()) {
                if (usuarioService.getUsuarioByRut(request.getRut()) != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse("El RUT ya está registrado"));
                }
            }

            // Validar contraseña
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("La contraseña es requerida"));
            }

            if (request.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("La contraseña debe tener al menos 6 caracteres"));
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(request.getNombre());
            usuario.setEmail(request.getEmail());
            usuario.setRut(request.getRut());
            usuario.setPassword(usuarioService.encryptPassword(request.getPassword()));
            usuario.setRol(RolUsuario.valueOf(request.getRol() != null ? request.getRol().toUpperCase() : "USER"));
            // Generar apiKey automáticamente como en el flujo de registro
            usuario.setApiKey(usuarioService.generateApiKey());
            usuario.setFechaCreacion(LocalDateTime.now());

            Usuario usuarioGuardado = usuarioService.saveUsuario(usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario creado exitosamente");
            response.put("usuario", usuarioGuardado);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
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

    /**
     * DTO para crear múltiples usuarios
     */
    public static class CreateBulkUsuarioRequest {
        private String nombre;
        private String email;
        private String rut;
        private String password;
        private String rol = "USER";

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRut() { return rut; }
        public void setRut(String rut) { this.rut = rut; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }

    /**
     * DTO para crear usuario personalizado (sin token y apikey)
     */
    public static class CreateCustomUsuarioRequest {
        private String nombre;
        private String email;
        private String rut;
        private String password;
        private String rol = "USER";

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRut() { return rut; }
        public void setRut(String rut) { this.rut = rut; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
}
