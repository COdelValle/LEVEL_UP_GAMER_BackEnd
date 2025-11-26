package com.level_up_gamer.BackEnd.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.level_up_gamer.BackEnd.DTO.Auth.AuthResponse;
import com.level_up_gamer.BackEnd.DTO.Auth.LoginRequest;
import com.level_up_gamer.BackEnd.DTO.Auth.RegisterRequest;
import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
import com.level_up_gamer.BackEnd.Repository.UsuarioRepository;
import com.level_up_gamer.BackEnd.Security.JwtProvider;
import com.level_up_gamer.BackEnd.Security.PasswordEncrypter;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private PasswordEncrypter passwordEncrypter;
    
    @Autowired
    private JwtProvider jwtProvider;

    // Obtener info:
        // Todos los Usuarios:
        public List<Usuario> getUsuarios(){
            return repository.findAll();
        }

        // Por ID:
        public Usuario getUsuarioByID(Long id){
            return repository.findById(id).orElse(null);
        }

        // Por email:
        public Usuario getUsuarioByEmail(String email){
            return repository.findByEmail(email).orElse(null);
        }
    
    // Guardar(agregar o actualizar) usuario:
    public Usuario saveUsuario(Usuario o){
        return repository.save(o);
    }

    // Encriptar contraseña:
    public String encryptPassword(String password) {
        return passwordEncrypter.encryptPassword(password);
    }

    // Eliminar Usuario:
        // Por Objeto:
        public Boolean deleteUsuario(Usuario o){
            repository.delete(o);
            return !repository.existsById(o.getId());
        }

        // Por ID:
        public Boolean deleteUsuarioById(Long id){
            repository.deleteById(id);
            return !repository.existsById(id);
        }

        // Por Email(String):
        public Boolean deleteUsuarioByemail(String email){
            repository.deleteByEmail(email);
            return !repository.existsByEmail(email);
        }
    
    // Autenticación:
        // Registra un usuario:
        public AuthResponse registrar(RegisterRequest request) throws Exception {
            // Validar que las contraseñas coincidan
            if (!request.getPassword().equals(request.getPasswordConfirm())) {
                throw new Exception("Las contraseñas no coinciden");
            }
            
            // Validar que el email no existe
            if (repository.existsByEmail(request.getEmail())) {
                throw new Exception("El email ya está registrado");
            }
            
            // Crear nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(request.getNombre());
            usuario.setEmail(request.getEmail());
            usuario.setPassword(passwordEncrypter.encryptPassword(request.getPassword()));
            usuario.setApiKey(passwordEncrypter.generateApiKey());
            usuario.setFechaCreacion(LocalDateTime.now());
            
            usuario = repository.save(usuario);
            
            // Generar token JWT
            String token = jwtProvider.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol().toString());
            
            return new AuthResponse(
                token,
                usuario.getApiKey(),
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().toString()
            );
        }
        
        // Iniciar sesión:
        public AuthResponse login(LoginRequest request) throws Exception {
            Usuario usuario = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
            
            if (!passwordEncrypter.matches(request.getPassword(), usuario.getPassword())) {
                throw new Exception("Contraseña incorrecta");
            }
            
            // Actualizar último acceso
            usuario.setUltimoAcceso(LocalDateTime.now());
            repository.save(usuario);
            
            // Generar token JWT
            String token = jwtProvider.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol().toString());
            
            return new AuthResponse(
                token,
                usuario.getApiKey(),
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().toString()
            );
        }
        
        // Valida que un Token este valido:
        public boolean validarToken(String token) {
            return jwtProvider.validateToken(token);
        }
        
        // Obtiene la información de un usuario a traves de un Token:
        public Usuario getUsuarioFromToken(String token) throws Exception {
            String email = jwtProvider.getEmailFromToken(token);
            return repository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
        }
        
        // Valida la API Key del usuario:
        public Usuario getUsuarioByApiKey(String apiKey) throws Exception {
            return repository.findByApiKey(apiKey)
                .orElseThrow(() -> new Exception("API Key inválida"));
        }
}
