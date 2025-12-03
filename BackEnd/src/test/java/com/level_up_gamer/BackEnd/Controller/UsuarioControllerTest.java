package com.level_up_gamer.BackEnd.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.level_up_gamer.BackEnd.Config.TestSecurityConfig;
import com.level_up_gamer.BackEnd.DTO.Usuario.UpdateUsuarioRequest;
import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@Import(TestSecurityConfig.class)
@DisplayName("UsuarioController Tests")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario testUsuario;

    @BeforeEach
    void setUp() {
        testUsuario = new Usuario();
        testUsuario.setId(1L);
        testUsuario.setNombre("Test User");
        testUsuario.setEmail("test@example.com");
        testUsuario.setRut("12345678-5");
    }

    // ==================== GET USUARIO - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Obtener usuario por ID - exitoso")
    void getUsuario_returnsOk_whenFound() throws Exception {
        when(usuarioService.getUsuarioByID(1L)).thenReturn(testUsuario);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.rut").value("12345678-5"));
    }

    @Test
    @DisplayName("Obtener usuario por RUT - exitoso")
    void getUsuarioByRut_returnsOk_whenFound() throws Exception {
        when(usuarioService.getUsuarioByRut("12345678-5")).thenReturn(testUsuario);

        mockMvc.perform(get("/api/v1/usuarios/rut/12345678-5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-5"));
    }

    // ==================== GET USUARIO - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Obtener usuario - no encontrado por ID")
    void getUsuario_returnsNotFound_whenNotExists() throws Exception {
        when(usuarioService.getUsuarioByID(999L)).thenThrow(new Exception("Usuario no encontrado"));

        mockMvc.perform(get("/api/v1/usuarios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Obtener usuario - no encontrado por RUT")
    void getUsuarioByRut_returnsNotFound_whenNotExists() throws Exception {
        when(usuarioService.getUsuarioByRut("99999999-9")).thenThrow(new Exception("Usuario no encontrado"));

        mockMvc.perform(get("/api/v1/usuarios/rut/99999999-9"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Obtener usuario - RUT inválido")
    void getUsuarioByRut_returnsBadRequest_whenRutInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/rut/invalid-rut"))
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE USUARIO - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Actualizar usuario - exitoso con datos válidos")
    void updateUsuario_returnsOk_whenValid() throws Exception {
        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setNombre("Updated User");
        req.setEmail("updated@example.com");
        req.setRut("87654321-1");

        testUsuario.setNombre("Updated User");
        testUsuario.setEmail("updated@example.com");
        testUsuario.setRut("87654321-1");

        when(usuarioService.saveUsuario(any())).thenReturn(testUsuario);

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.rut").value("87654321-1"));
    }

    @Test
    @DisplayName("Actualizar usuario - solo nombre")
    void updateUsuario_returnsOk_whenOnlyNameUpdated() throws Exception {
        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setNombre("New Name");

        testUsuario.setNombre("New Name");

        when(usuarioService.saveUsuario(any())).thenReturn(testUsuario);

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("New Name"));
    }

    // ==================== UPDATE USUARIO - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Actualizar usuario - nombre vacío")
    void updateUsuario_failsWhenNameBlank() throws Exception {
        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setNombre("");

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Actualizar usuario - email inválido")
    void updateUsuario_failsWhenEmailInvalid() throws Exception {
        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setEmail("invalid-email");

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Actualizar usuario - RUT inválido")
    void updateUsuario_failsWhenRutInvalid() throws Exception {
        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setRut("99999999-9");

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Actualizar usuario - usuario no encontrado")
    void updateUsuario_returnsNotFound_whenUserNotExists() throws Exception {
        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setNombre("New Name");

        when(usuarioService.getUsuarioByID(999L)).thenReturn(null);

        mockMvc.perform(put("/api/v1/usuarios/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Actualizar usuario - email ya existe")
    void updateUsuario_failsWhenEmailAlreadyExists() throws Exception {
        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setEmail("existing@example.com");

        when(usuarioService.getUsuarioByEmail("existing@example.com")).thenReturn(new Usuario());

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Actualizar usuario - RUT ya existe")
    void updateUsuario_failsWhenRutAlreadyExists() throws Exception {
        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setRut("11111111-1");

        when(usuarioService.getUsuarioByRut("11111111-1")).thenReturn(new Usuario());

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    // ==================== DELETE USUARIO - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Eliminar usuario - exitoso")
    void deleteUsuario_returnsNoContent_whenSuccessful() throws Exception {
        when(usuarioService.deleteUsuarioById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    // ==================== DELETE USUARIO - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Eliminar usuario - no encontrado")
    void deleteUsuario_returnsNotFound_whenNotExists() throws Exception {
        when(usuarioService.deleteUsuarioById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/usuarios/999"))
                .andExpect(status().isNotFound());
    }

    // ==================== GET ALL USUARIOS - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Obtener todos los usuarios - exitoso")
    void getAllUsuarios_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk());
    }

    // ==================== CREAR MULTIPLES USUARIOS ====================

    @Test
    @DisplayName("Crear múltiples usuarios - exitoso")
    void createBulkUsuarios_returnsCreated_whenValid() throws Exception {
        String json = "[{\"nombre\":\"User1\",\"email\":\"user1@example.com\",\"rut\":\"12345678-5\",\"password\":\"Pass123\",\"passwordConfirm\":\"Pass123\"}]";

        mockMvc.perform(post("/api/v1/usuarios/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Crear múltiples usuarios - falla con datos inválidos")
    void createBulkUsuarios_failsWhenInvalid() throws Exception {
        String json = "[{\"nombre\":\"\",\"email\":\"user1@example.com\",\"rut\":\"12345678-5\",\"password\":\"Pass123\",\"passwordConfirm\":\"Pass123\"}]";

        mockMvc.perform(post("/api/v1/usuarios/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
