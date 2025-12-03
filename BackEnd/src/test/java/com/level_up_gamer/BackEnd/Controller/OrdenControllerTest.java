package com.level_up_gamer.BackEnd.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.level_up_gamer.BackEnd.Config.TestSecurityConfig;
import com.level_up_gamer.BackEnd.Model.Orden.Orden;
import com.level_up_gamer.BackEnd.Service.Orden.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdenController.class)
@Import(TestSecurityConfig.class)
@DisplayName("OrdenController Tests")
public class OrdenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenService ordenService;

    @Autowired
    private ObjectMapper objectMapper;

    private Orden testOrden;

    @BeforeEach
    void setUp() {
        testOrden = new Orden();
        testOrden.setId(1L);
        testOrden.setNumero("ORD-001");
        testOrden.setTotal(299);
        testOrden.setFecha(LocalDateTime.now());
    }

    // ==================== GET ORDEN - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Obtener todas las órdenes - exitoso")
    void obtenerTodas_returnsOk() throws Exception {
        when(ordenService.getOrdenes()).thenReturn(Collections.singletonList(testOrden));

        mockMvc.perform(get("/api/v1/ordenes"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Obtener orden por ID - exitoso")
    void obtenerPorId_returnsOk_whenFound() throws Exception {
        when(ordenService.getOrdenByID(1L)).thenReturn(testOrden);

        mockMvc.perform(get("/api/v1/ordenes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value("ORD-001"));
    }

    @Test
    @DisplayName("Obtener órdenes por estado - exitoso")
    void obtenerPorEstado_returnsOk() throws Exception {
        when(ordenService.getOrdenes()).thenReturn(Collections.singletonList(testOrden));

        mockMvc.perform(get("/api/v1/ordenes/estado/PENDIENTE"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Obtener órdenes por usuario - exitoso")
    void obtenerPorUsuario_returnsOk() throws Exception {
        when(ordenService.getOrdenes()).thenReturn(Collections.singletonList(testOrden));

        mockMvc.perform(get("/api/v1/ordenes/usuario/1"))
                .andExpect(status().isOk());
    }

    // ==================== GET ORDEN - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Obtener orden - no encontrada por ID")
    void obtenerPorId_returnsNotFound_whenNotExists() throws Exception {
        when(ordenService.getOrdenByID(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/ordenes/999"))
                .andExpect(status().isNotFound());
    }

    // ==================== CREATE ORDEN - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Crear orden - exitoso con datos válidos")
    void crearOrden_returnsCreated_whenValid() throws Exception {
        String json = "{\"numero\":\"ORD-002\",\"total\":199,\"estado\":\"PENDIENTE\",\"metodoPago\":\"PAYPAL\"}";

        when(ordenService.saveOrden(any())).thenReturn(testOrden);

        mockMvc.perform(post("/api/v1/ordenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    // ==================== CREATE ORDEN - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Crear orden - falla sin número")
    void crearOrden_failsWhenNumberBlank() throws Exception {
        String json = "{\"numero\":\"\",\"total\":199,\"estado\":\"PENDIENTE\",\"metodoPago\":\"PAYPAL\"}";

        mockMvc.perform(post("/api/v1/ordenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear orden - falla con total negativo")
    void crearOrden_failsWhenTotalNegative() throws Exception {
        String json = "{\"numero\":\"ORD-003\",\"total\":-50,\"estado\":\"PENDIENTE\",\"metodoPago\":\"PAYPAL\"}";

        mockMvc.perform(post("/api/v1/ordenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear orden - falla sin estado")
    void crearOrden_failsWhenStateBlank() throws Exception {
        String json = "{\"numero\":\"ORD-004\",\"total\":199,\"estado\":\"\",\"metodoPago\":\"PAYPAL\"}";

        mockMvc.perform(post("/api/v1/ordenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear orden - falla sin método de pago")
    void crearOrden_failsWhenPaymentMethodBlank() throws Exception {
        String json = "{\"numero\":\"ORD-005\",\"total\":199,\"estado\":\"PENDIENTE\",\"metodoPago\":\"\"}";

        mockMvc.perform(post("/api/v1/ordenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE ORDEN - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Actualizar orden - cambio de estado exitoso")
    void actualizarOrden_returnsOk_whenStateChanged() throws Exception {
        String json = "{\"numero\":\"ORD-001\",\"total\":299,\"estado\":\"ENTREGADO\",\"metodoPago\":\"TARJETA_CREDITO\"}";

        when(ordenService.saveOrden(any())).thenReturn(testOrden);

        mockMvc.perform(put("/api/v1/ordenes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    // ==================== UPDATE ORDEN - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Actualizar orden - orden no encontrada")
    void actualizarOrden_returnsNotFound_whenNotExists() throws Exception {
        String json = "{\"numero\":\"ORD-001\",\"total\":299,\"estado\":\"ENTREGADO\",\"metodoPago\":\"TARJETA_CREDITO\"}";

        when(ordenService.saveOrden(any())).thenReturn(null);

        mockMvc.perform(put("/api/v1/ordenes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE ORDEN - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Eliminar orden - exitoso")
    void eliminarOrden_returnsNoContent_whenSuccessful() throws Exception {
        when(ordenService.deleteOrdenById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/ordenes/1"))
                .andExpect(status().isNoContent());
    }

    // ==================== DELETE ORDEN - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Eliminar orden - no encontrada")
    void eliminarOrden_returnsNotFound_whenNotExists() throws Exception {
        when(ordenService.deleteOrdenById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/ordenes/999"))
                .andExpect(status().isNotFound());
    }
}
