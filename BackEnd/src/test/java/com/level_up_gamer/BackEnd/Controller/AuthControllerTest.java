package com.level_up_gamer.BackEnd.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.level_up_gamer.BackEnd.Config.TestSecurityConfig;
import com.level_up_gamer.BackEnd.DTO.Auth.AuthResponse;
import com.level_up_gamer.BackEnd.DTO.Auth.LoginRequest;
import com.level_up_gamer.BackEnd.DTO.Auth.RegisterRequest;
import com.level_up_gamer.BackEnd.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
@DisplayName("AuthController Tests")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthResponse validAuthResponse;

    @BeforeEach
    void setUp() {
        validAuthResponse = new AuthResponse("test-token", "test-apikey", 1L, "Test User", "test@example.com", "USER");
    }

    // ==================== REGISTRO - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Registro exitoso con datos válidos")
    void register_returnsCreated_whenValid() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Benjamin García");
        req.setEmail("benjamin@levelup.cl");
        req.setRut("12345678-5");
        req.setPassword("SecurePass123");
        req.setPasswordConfirm("SecurePass123");

        when(usuarioService.registrar(any())).thenReturn(validAuthResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.apiKey").value("test-apikey"))
                .andExpect(jsonPath("$.usuarioId").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Registro con RUT válido en formato sin guión")
    void register_acceptsRutWithoutDash() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Juan Perez");
        req.setEmail("juan@levelup.cl");
        req.setRut("123456785");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        when(usuarioService.registrar(any())).thenReturn(validAuthResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    // ==================== REGISTRO - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Registro falla - nombre vacío")
    void register_failsWhenNameBlank() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("");
        req.setEmail("test@example.com");
        req.setRut("12345678-5");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - nombre muy corto (1 caracter)")
    void register_failsWhenNameTooShort() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("A");
        req.setEmail("test@example.com");
        req.setRut("12345678-5");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - email inválido")
    void register_failsWhenEmailInvalid() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("invalid-email-format");
        req.setRut("12345678-5");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - email vacío")
    void register_failsWhenEmailBlank() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("");
        req.setRut("12345678-5");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - RUT inválido")
    void register_failsWhenRutInvalid() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("test@example.com");
        req.setRut("99999999-9");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - RUT vacío")
    void register_failsWhenRutBlank() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("test@example.com");
        req.setRut("");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - contraseña muy corta (<6 caracteres)")
    void register_failsWhenPasswordTooShort() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("test@example.com");
        req.setRut("12345678-5");
        req.setPassword("Pass1");
        req.setPasswordConfirm("Pass1");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - contraseña vacía")
    void register_failsWhenPasswordBlank() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("test@example.com");
        req.setRut("12345678-5");
        req.setPassword("");
        req.setPasswordConfirm("");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - contraseñas no coinciden")
    void register_failsWhenPasswordsDoNotMatch() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("test@example.com");
        req.setRut("12345678-5");
        req.setPassword("Password123");
        req.setPasswordConfirm("Password456");

        when(usuarioService.registrar(any())).thenThrow(new Exception("Las contraseñas no coinciden"));

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - email ya existe")
    void register_failsWhenEmailAlreadyExists() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("existing@example.com");
        req.setRut("12345678-5");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        when(usuarioService.registrar(any())).thenThrow(new Exception("El email ya está registrado"));

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Registro falla - RUT ya existe")
    void register_failsWhenRutAlreadyExists() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNombre("Test User");
        req.setEmail("test@example.com");
        req.setRut("11111111-1");
        req.setPassword("Pass12345");
        req.setPasswordConfirm("Pass12345");

        when(usuarioService.registrar(any())).thenThrow(new Exception("El RUT ya está registrado"));

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ==================== LOGIN - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Login exitoso con credenciales válidas")
    void login_returnsOk_whenCredentialsValid() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("test@example.com");
        req.setPassword("Pass12345");

        when(usuarioService.login(any())).thenReturn(validAuthResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.usuarioId").value(1L));
    }

    // ==================== LOGIN - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Login falla - usuario no encontrado")
    void login_failsWhenUserNotFound() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("nonexistent@example.com");
        req.setPassword("Pass12345");

        when(usuarioService.login(any())).thenThrow(new Exception("Usuario no encontrado"));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login falla - contraseña incorrecta")
    void login_failsWhenPasswordIncorrect() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("test@example.com");
        req.setPassword("WrongPassword");

        when(usuarioService.login(any())).thenThrow(new Exception("Contraseña incorrecta"));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login falla - email vacío")
    void login_failsWhenEmailBlank() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("");
        req.setPassword("Pass12345");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login falla - contraseña vacía")
    void login_failsWhenPasswordBlank() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("test@example.com");
        req.setPassword("");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login falla - email inválido")
    void login_failsWhenEmailInvalid() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail("not-an-email");
        req.setPassword("Pass12345");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
