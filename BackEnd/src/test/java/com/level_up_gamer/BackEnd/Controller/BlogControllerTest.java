package com.level_up_gamer.BackEnd.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.level_up_gamer.BackEnd.Config.TestSecurityConfig;
import com.level_up_gamer.BackEnd.Model.Blog.Blog;
import com.level_up_gamer.BackEnd.Service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
@Import(TestSecurityConfig.class)
@DisplayName("BlogController Tests")
public class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Autowired
    private ObjectMapper objectMapper;

    private Blog testBlog;

    @BeforeEach
    void setUp() {
        testBlog = new Blog();
        testBlog.setId(1L);
        testBlog.setTitle("Test Blog Title");
        testBlog.setExcerpt("This is a test excerpt");
        testBlog.setContent("This is the full test content for the blog post");
        testBlog.setAuthor("Test Author");
        testBlog.setFecha(LocalDate.now());
    }

    // ==================== GET BLOG - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Obtener todos los blogs - exitoso")
    void obtenerTodos_returnsOk() throws Exception {
        when(blogService.getBlogs()).thenReturn(Collections.singletonList(testBlog));

        mockMvc.perform(get("/api/v1/blog"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Obtener blog por ID - exitoso")
    void obtenerPorId_returnsOk_whenFound() throws Exception {
        when(blogService.getBlogByID(1L)).thenReturn(testBlog);

        mockMvc.perform(get("/api/v1/blog/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Blog Title"));
    }

    // ==================== GET BLOG - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Obtener blog - no encontrado")
    void obtenerPorId_returnsNotFound_whenNotExists() throws Exception {
        when(blogService.getBlogByID(999L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/blog/999"))
                .andExpect(status().isNotFound());
    }

    // ==================== CREATE BLOG - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Crear blog - exitoso con datos válidos")
    void crearBlog_returnsCreated_whenValid() throws Exception {
        String json = "{\"title\":\"New Blog\",\"excerpt\":\"Excerpt text\",\"content\":\"Full content here\",\"author\":\"Author Name\"}";

        when(blogService.saveBlog(any())).thenReturn(testBlog);

        mockMvc.perform(post("/api/v1/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    // ==================== CREATE BLOG - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Crear blog - falla sin título")
    void crearBlog_failsWhenTitleBlank() throws Exception {
        String json = "{\"title\":\"\",\"excerpt\":\"Excerpt text\",\"content\":\"Full content here\",\"author\":\"Author Name\"}";

        mockMvc.perform(post("/api/v1/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear blog - falla sin excerpt")
    void crearBlog_failsWhenExcerptBlank() throws Exception {
        String json = "{\"title\":\"New Blog\",\"excerpt\":\"\",\"content\":\"Full content here\",\"author\":\"Author Name\"}";

        mockMvc.perform(post("/api/v1/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear blog - falla sin contenido")
    void crearBlog_failsWhenContentBlank() throws Exception {
        String json = "{\"title\":\"New Blog\",\"excerpt\":\"Excerpt text\",\"content\":\"\",\"author\":\"Author Name\"}";

        mockMvc.perform(post("/api/v1/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Crear blog - falla sin autor")
    void crearBlog_failsWhenAuthorBlank() throws Exception {
        String json = "{\"title\":\"New Blog\",\"excerpt\":\"Excerpt text\",\"content\":\"Full content here\",\"author\":\"\"}";

        mockMvc.perform(post("/api/v1/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE BLOG - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Actualizar blog - exitoso")
    void actualizarBlog_returnsOk_whenValid() throws Exception {
        String json = "{\"title\":\"Updated Blog\",\"excerpt\":\"Updated excerpt\",\"content\":\"Updated content\",\"author\":\"Updated Author\"}";

        testBlog.setTitle("Updated Blog");
        when(blogService.saveBlog(any())).thenReturn(testBlog);

        mockMvc.perform(put("/api/v1/blog/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Blog"));
    }

    // ==================== UPDATE BLOG - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Actualizar blog - blog no encontrado")
    void actualizarBlog_returnsNotFound_whenNotExists() throws Exception {
        String json = "{\"title\":\"Updated Blog\",\"excerpt\":\"Excerpt\",\"content\":\"Content\",\"author\":\"Author\"}";

        when(blogService.saveBlog(any())).thenReturn(null);

        mockMvc.perform(put("/api/v1/blog/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE BLOG - CASOS CORRECTOS ====================

    @Test
    @DisplayName("Eliminar blog - exitoso")
    void eliminarBlog_returnsNoContent_whenSuccessful() throws Exception {
        mockMvc.perform(delete("/api/v1/blog/1"))
                .andExpect(status().isNoContent());
    }

    // ==================== DELETE BLOG - CASOS INCORRECTOS ====================

    @Test
    @DisplayName("Eliminar blog - no encontrado")
    void eliminarBlog_returnsNotFound_whenNotExists() throws Exception {
        when(blogService.deleteBlogById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/blog/999"))
                .andExpect(status().isNotFound());
    }
}
