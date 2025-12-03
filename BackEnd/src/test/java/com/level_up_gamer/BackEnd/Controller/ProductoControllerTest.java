package com.level_up_gamer.BackEnd.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.level_up_gamer.BackEnd.Config.TestSecurityConfig;
import com.level_up_gamer.BackEnd.Model.Producto.Producto;
import com.level_up_gamer.BackEnd.Service.Producto.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@Import(TestSecurityConfig.class)
@DisplayName("ProductoController Tests")
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto testProducto;

    @BeforeEach
    void setUp() {
        testProducto = new Producto();
        testProducto.setId(1L);
        testProducto.setNombre("Test Producto");
        testProducto.setPrecio(9999);
        testProducto.setCategoria("Gaming");
        testProducto.setStock(10);
        testProducto.setImagen("image.jpg");
    }

    // ==================== GET PRODUCTO - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Obtener todos los productos - exitoso")
    void obtenerTodos_returnsOk() throws Exception {
        when(productoService.getProductos()).thenReturn(Collections.singletonList(testProducto));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Obtener producto por ID - exitoso")
    void obtenerPorId_returnsOk_whenFound() throws Exception {
        when(productoService.getProductoByID(1L)).thenReturn(testProducto);

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Test Producto"))
                .andExpect(jsonPath("$.precio").value(99.99));
    }

    @Test
    @DisplayName("Obtener productos por categoría - exitoso")
    void obtenerPorCategoria_returnsOk() throws Exception {
        when(productoService.getProductos()).thenReturn(Collections.singletonList(testProducto));

        mockMvc.perform(get("/api/v1/productos/categoria/Gaming"))
                .andExpect(status().isOk());
    }

    // ==================== GET PRODUCTO - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Obtener producto - no encontrado por ID")
    void obtenerPorId_returnsNotFound_whenNotExists() throws Exception {
        when(productoService.getProductoByID(999L)).thenThrow(new Exception("Producto no encontrado"));

        mockMvc.perform(get("/api/v1/productos/999"))
                .andExpect(status().isNotFound());
    }

    // ==================== CREATE PRODUCTO - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Crear producto - exitoso con datos válidos")
    void crearProducto_returnsCreated_whenValid() throws Exception {
        String json = "{\"nombre\":\"New Producto\",\"precio\":49.99,\"categoria\":\"Gaming\",\"stock\":5,\"imagen\":\"image.jpg\"}";

        when(productoService.saveProducto(any())).thenReturn(testProducto);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    // ==================== CREATE PRODUCTO - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Crear producto - falla sin nombre")
    void crearProducto_failsWhenNameBlank() throws Exception {
        String json = "{\"nombre\":\"\",\"precio\":49.99,\"categoria\":\"Gaming\",\"stock\":5,\"imagen\":\"image.jpg\"}";

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear producto - falla con precio negativo")
    void crearProducto_failsWhenPriceNegative() throws Exception {
        String json = "{\"nombre\":\"Test\",\"precio\":-10.00,\"categoria\":\"Gaming\",\"stock\":5,\"imagen\":\"image.jpg\"}";

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear producto - falla con stock negativo")
    void crearProducto_failsWhenStockNegative() throws Exception {
        String json = "{\"nombre\":\"Test\",\"precio\":49.99,\"categoria\":\"Gaming\",\"stock\":-1,\"imagen\":\"image.jpg\"}";

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear producto - falla sin categoría")
    void crearProducto_failsWhenCategoryBlank() throws Exception {
        String json = "{\"nombre\":\"Test\",\"precio\":49.99,\"categoria\":\"\",\"stock\":5,\"imagen\":\"image.jpg\"}";

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE PRODUCTO - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Actualizar producto - exitoso")
    void actualizarProducto_returnsOk_whenValid() throws Exception {
        String json = "{\"nombre\":\"Updated Producto\",\"precio\":5999,\"categoria\":\"Gaming\",\"stock\":8,\"imagen\":\"updated.jpg\"}";

        testProducto.setNombre("Updated Producto");
        when(productoService.saveProducto(any())).thenReturn(testProducto);

        mockMvc.perform(put("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Updated Producto"));
    }

    // ==================== UPDATE PRODUCTO - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Actualizar producto - producto no encontrado")
    void actualizarProducto_returnsNotFound_whenNotExists() throws Exception {
        String json = "{\"nombre\":\"Test\",\"precio\":49.99,\"categoria\":\"Gaming\",\"stock\":5,\"imagen\":\"image.jpg\"}";

        when(productoService.saveProducto(any())).thenThrow(new Exception("Producto no encontrado"));

        mockMvc.perform(put("/api/v1/productos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE PRODUCTO - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Eliminar producto - exitoso")
    void eliminarProducto_returnsNoContent_whenSuccessful() throws Exception {
        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());
    }

    // ==================== DELETE PRODUCTO - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Eliminar producto - no encontrado")
    void eliminarProducto_returnsNotFound_whenNotExists() throws Exception {
        when(productoService.deleteProductoById(999L)).thenThrow(new Exception("Producto no encontrado"));

        mockMvc.perform(delete("/api/v1/productos/999"))
                .andExpect(status().isNotFound());
    }
}
