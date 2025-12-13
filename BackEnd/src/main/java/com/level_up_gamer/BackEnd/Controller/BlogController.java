package com.level_up_gamer.BackEnd.Controller;

import com.level_up_gamer.BackEnd.DTO.Blog.CreateBlogRequest;
import com.level_up_gamer.BackEnd.DTO.Blog.BlogResponse;
import com.level_up_gamer.BackEnd.Model.Blog.Blog;
import com.level_up_gamer.BackEnd.Model.Blog.CategoriaBlog;
import com.level_up_gamer.BackEnd.Service.BlogService;
import com.level_up_gamer.BackEnd.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para gestionar artículos de Blog en LEVEL UP GAMER
 * 
 * Proporciona endpoints para:
 * - Obtener artículos del blog
 * - Buscar y filtrar por categoría o autor
 * - Crear nuevos artículos (solo ADMIN/EDITOR)
 * - Actualizar artículos existentes
 * - Eliminar artículos
 * - Gestionar comentarios y reacciones
 * 
 * @author LEVEL UP GAMER Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/blog")
@Tag(name = "Blog", description = "API para gestión de contenido del blog. GET es público, POST/PUT/DELETE requiere autenticación. Incluye artículos, categorías y búsqueda.")
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * Obtiene todos los artículos del blog
     * 
     * Información incluida:
     * - Título y resumen
     * - Contenido completo
     * - Autor
     * - Fecha de publicación
     * - Categoría
     * - Tiempo estimado de lectura
     * - Imagen de portada
     * 
     * Acceso: Público (no requiere autenticación)
     * 
     * @return ResponseEntity con lista de BlogResponse
     */
    @GetMapping
    @Operation(
            summary = "Obtener todos los artículos",
            description = "Retorna una lista completa de todos los artículos publicados en el blog. " +
                    "Incluye resumen, autor, categoría y metadatos de cada artículo. Acceso: Público.",
            tags = {"Blog"}
    )
    @ApiResponse(
            responseCode = "200",
            description = "Artículos obtenidos exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BlogResponse.class))
            )
    )
    public ResponseEntity<?> obtenerTodos() {
        List<Blog> blogs = blogService.getBlogs();
        List<BlogResponse> response = blogs.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un artículo específico por su ID
     * 
     * Útil para:
     * - Leer el artículo completo
     * - Ver detalles del autor
     * - Gestionar comentarios
     * - Mostrar artículos relacionados
     * 
     * Acceso: Público (no requiere autenticación)
     * 
     * @param id ID único del artículo
     * @return ResponseEntity con BlogResponse
     * @throws ResourceNotFoundException si el artículo no existe
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener artículo por ID",
        description = "Retorna el contenido completo de un artículo específico del blog. " +
                "Incluye todos los detalles, autor, categoría e información de lectura. Acceso: Público.",
        tags = {"Blog"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Artículo encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Artículo no encontrado",
        content = @Content(mediaType = "application/json")
    )
    public ResponseEntity<?> obtenerPorId(
        @Parameter(description = "ID único del artículo", required = true, example = "1")
        @PathVariable Long id) {
        
        Blog blog = blogService.getBlogByID(id);
        if (blog == null) {
            throw new ResourceNotFoundException("Artículo con ID " + id + " no encontrado");
        }
        
        return ResponseEntity.ok(mapearAResponse(blog));
    }

    /**
     * Obtiene artículos por autor
     * 
     * Útil para:
     * - Ver todos los artículos de un autor específico
     * - Profundizar en temáticas de expertos
     * - Mostrar autor en el perfil
     * 
     * Acceso: Público (no requiere autenticación)
     * 
     * @param autor Nombre del autor a filtrar
     * @return ResponseEntity con lista de BlogResponse
     */
    @GetMapping("/autor/{autor}")
    @Operation(
        summary = "Obtener artículos por autor",
        description = "Retorna todos los artículos publicados por un autor específico. " +
                "Útil para explorar el trabajo de expertos en gaming. Acceso: Público.",
        tags = {"Blog"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Artículos del autor obtenidos exitosamente",
        content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = BlogResponse.class))
        )
    )
    public ResponseEntity<?> obtenerPorAutor(
        @Parameter(description = "Nombre del autor a filtrar", required = true, example = "Juan García")
        @PathVariable String autor) {
        
        List<Blog> blogs = blogService.getBlogByNombre(autor);
        List<BlogResponse> response = blogs.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Crea un nuevo artículo en el blog
     * 
     * Validaciones:
     * - Título: Requerido, entre 10 y 200 caracteres
     * - Contenido: Requerido, mínimo 100 caracteres
     * - Excerpt (Resumen): Requerido, entre 50 y 500 caracteres
     * - Autor: Requerido
     * - Categoría: Requerido (Gaming, Reviews, Tutoriales, Noticias, Competitivo)
     * 
     * Proceso:
     * 1. Valida todos los campos requeridos
     * 2. Calcula tiempo de lectura automáticamente
     * 3. Crea el artículo con fecha actual
     * 4. Publica inmediatamente
     * 
     * Acceso: Solo ADMIN y EDITOR (requiere autenticación y rol)
     * 
     * @param request CreateBlogRequest validado
     * @return ResponseEntity con BlogResponse del artículo creado
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKeyAuth")
    @Operation(
        summary = "Crear nuevo artículo",
        description = "Crea un nuevo artículo en el blog. Solo administradores y editores pueden " +
                "realizar esta operación. El artículo se publica inmediatamente.",
        tags = {"Blog"}
    )
    @ApiResponse(
        responseCode = "201",
        description = "Artículo creado exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogResponse.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "401",
        description = "No autenticado",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "403",
        description = "Usuario no tiene permiso para crear artículos",
        content = @Content(mediaType = "application/json")
    )
    public ResponseEntity<?> crear(
            @Valid @RequestBody CreateBlogRequest request) {
        
        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setExcerpt(request.getExcerpt());
        blog.setContent(request.getContent());
        blog.setCategoria(CategoriaBlog.valueOf(request.getCategoria().toUpperCase()));
        blog.setAuthor(request.getAuthor());
        blog.setFecha(java.time.LocalDate.now());
        blog.setImage(request.getImage());
        blog.setGradient(request.getGradient());
        
        Blog blogGuardado = blogService.saveBlog(blog);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapearAResponse(blogGuardado));
    }

    /**
     * Crea múltiples artículos en el blog
     * Acceso: Solo ADMIN
     */
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKeyAuth")
    @Operation(
        summary = "Crear múltiples artículos",
        description = "Crea varios artículos en el blog en una sola solicitud",
        tags = {"Blog"}
    )
    @ApiResponse(
        responseCode = "201",
        description = "Artículos creados exitosamente",
        content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = BlogResponse.class))
        )
    )
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "403", description = "No tiene permisos (solo ADMIN)")
    public ResponseEntity<?> crearMultiples(@Valid @RequestBody List<CreateBlogRequest> requests) {
        List<Blog> blogs = requests.stream().map(request -> {
            Blog blog = new Blog();
            blog.setTitle(request.getTitle());
            blog.setExcerpt(request.getExcerpt());
            blog.setContent(request.getContent());
            blog.setCategoria(CategoriaBlog.valueOf(request.getCategoria().toUpperCase()));
            blog.setAuthor(request.getAuthor());
            blog.setFecha(java.time.LocalDate.now());
            blog.setImage(request.getImage());
            blog.setGradient(request.getGradient());
            return blog;
        }).collect(Collectors.toList());

        List<Blog> blogsGuardados = blogService.saveAllBlogs(blogs);
        List<BlogResponse> response = blogsGuardados.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un artículo existente
     * 
     * Campos actualizables:
     * - Título
     * - Contenido y excerpt
     * - Categoría
     * - Imagen de portada
     * - Gradiente de tema
     * 
     * Acceso: Solo ADMIN y EDITOR (requiere autenticación y rol)
     * 
     * @param id ID del artículo a actualizar
     * @param request Datos actualizados
     * @return ResponseEntity con BlogResponse actualizado
     * @throws ResourceNotFoundException si el artículo no existe
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKeyAuth")
    @Operation(
        summary = "Actualizar artículo",
        description = "Actualiza los detalles de un artículo existente. Solo administradores " +
                "y editores pueden realizar esta operación.",
        tags = {"Blog"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Artículo actualizado exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogResponse.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Artículo no encontrado",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "403",
        description = "Usuario no tiene permiso",
        content = @Content(mediaType = "application/json")
    )
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del artículo a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CreateBlogRequest request) {
        
        Blog blog = blogService.getBlogByID(id);
        if (blog == null) {
            throw new ResourceNotFoundException("Artículo no encontrado");
        }
        
        blog.setTitle(request.getTitle());
        blog.setExcerpt(request.getExcerpt());
        blog.setContent(request.getContent());
        blog.setCategoria(CategoriaBlog.valueOf(request.getCategoria().toUpperCase()));
        blog.setImage(request.getImage());
        blog.setGradient(request.getGradient());
        
        Blog actualizado = blogService.saveBlog(blog);
        return ResponseEntity.ok(mapearAResponse(actualizado));
    }

    /**
     * Elimina un artículo del blog
     * 
     * Consideraciones:
     * - Se puede hacer soft delete (marcar como inactivo)
     * - Los comentarios se conservan para referencia histórica
     * - Las vistas y estadísticas se mantienen
     * 
     * Acceso: Solo ADMIN (requiere autenticación y rol ADMIN)
     * 
     * @param id ID del artículo a eliminar
     * @return ResponseEntity con mensaje de confirmación
     * @throws ResourceNotFoundException si el artículo no existe
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "apiKeyAuth")
    @Operation(
        summary = "Eliminar artículo",
        description = "Elimina un artículo del blog. Esta operación solo puede ser realizada " +
                "por administradores. El artículo se elimina permanentemente.",
        tags = {"Blog"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Artículo eliminado exitosamente",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "404",
        description = "Artículo no encontrado",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "403",
        description = "Usuario no tiene permiso (solo ADMIN)",
        content = @Content(mediaType = "application/json")
    )
    public ResponseEntity<?> eliminar(
        @Parameter(description = "ID del artículo a eliminar", required = true, example = "1")
        @PathVariable Long id) {
        
        Blog blog = blogService.getBlogByID(id);
        if (blog == null) {
            throw new ResourceNotFoundException("Artículo no encontrado");
        }
        
        blogService.deleteBlog(blog);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Artículo eliminado exitosamente");
        response.put("id", id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene artículos destacados para la portada
     * 
     * Útil para:
     * - Mostrar artículos populares
     * - Sección principal del blog
     * - Recomendaciones editoriales
     * 
     * Acceso: Público (no requiere autenticación)
     * 
     * @return ResponseEntity con lista de BlogResponse destacados
     */
    @GetMapping("/destacados")
    @Operation(
            summary = "Obtener artículos destacados",
            description = "Retorna los artículos marcados como destacados para mostrar " +
                    "en la portada del blog. Acceso: Público.",
            tags = {"Blog"}
    )
    @ApiResponse(
            responseCode = "200",
            description = "Artículos destacados obtenidos",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BlogResponse.class))
            )
    )
    public ResponseEntity<?> obtenerDestacados() {
        List<Blog> blogs = blogService.getBlogs();
        List<BlogResponse> response = blogs.stream()
                .limit(6)  // Mostrar máximo 6 destacados
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Mapea una entidad Blog a su DTO de respuesta
     * 
     * @param blog Entidad Blog de la base de datos
     * @return BlogResponse con datos formateados
     */
    private BlogResponse mapearAResponse(Blog blog) {
        BlogResponse response = new BlogResponse();
        response.setId(blog.getId());
        response.setTitle(blog.getTitle());
        response.setExcerpt(blog.getExcerpt());
        response.setContent(blog.getContent());
        response.setCategoria(blog.getCategoria().toString());
        response.setAuthor(blog.getAuthor());
        response.setFecha(blog.getFecha());
        response.setReadTime(blog.getReadTime());
        response.setImage(blog.getImage());
        response.setGradient(blog.getGradient());
        return response;
    }
}
