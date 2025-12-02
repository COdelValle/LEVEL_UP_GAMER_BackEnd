package com.level_up_gamer.BackEnd.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.level_up_gamer.BackEnd.DTO.Usuario.UpdateUsuarioRequest;
import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
import com.level_up_gamer.BackEnd.Service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllUsuarios_returnsOk() throws Exception {
        when(usuarioService.getUsuarios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUsuario_allowsUpdateRutAndEmail() throws Exception {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Old");
        u.setEmail("old@example.com");
        u.setRut("11111111-1");

        when(usuarioService.getUsuarioByID(anyLong())).thenReturn(u);
        when(usuarioService.getUsuarioByEmail(any())).thenReturn(null);
        when(usuarioService.getUsuarioByRut(any())).thenReturn(null);
        when(usuarioService.saveUsuario(any())).thenReturn(u);

        UpdateUsuarioRequest req = new UpdateUsuarioRequest();
        req.setNombre("New Name");
        req.setEmail("new@example.com");
        req.setRut("12345678-5");

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
