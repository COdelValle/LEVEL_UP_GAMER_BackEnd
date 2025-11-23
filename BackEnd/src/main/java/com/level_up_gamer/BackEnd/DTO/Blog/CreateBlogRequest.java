package com.level_up_gamer.BackEnd.DTO.Blog;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para crear y actualizar artículos de blog
 * Usado en: POST /api/blog (crear) y PUT /api/blog/{id} (actualizar)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para crear o actualizar un artículo de blog")
public class CreateBlogRequest {
    
    @NotBlank(message = "El título es requerido")
    @Size(min = 5, max = 200, message = "El título debe tener entre 5 y 200 caracteres")
    @Schema(description = "Título del artículo", example = "Guía Completa: Optimizar tu PC para Gaming")
    private String title;
    
    @NotBlank(message = "El resumen es requerido")
    @Size(min = 10, max = 500, message = "El resumen debe tener entre 10 y 500 caracteres")
    @Schema(description = "Resumen breve del artículo", example = "Aprende los mejores trucos para maximizar...")
    private String excerpt;
    
    @NotBlank(message = "El contenido es requerido")
    @Size(min = 50, message = "El contenido debe tener al menos 50 caracteres")
    @Schema(description = "Contenido completo del artículo en HTML o Markdown", example = "En este artículo vamos a explorar...")
    private String content;
    
    @NotBlank(message = "La categoría es requerida")
    @Schema(description = "Categoría del artículo", example = "Gaming")
    private String categoria;
    
    @NotBlank(message = "El autor es requerido")
    @Size(min = 2, max = 100, message = "El autor debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del autor del artículo", example = "Juan García")
    private String author;
    
    @Schema(description = "Tiempo estimado de lectura", example = "5 minutos")
    private String readTime;
    
    @Schema(description = "URL de la imagen de portada", example = "https://cdn.example.com/blog/gaming-guide.jpg")
    private String image;
    
    @Schema(description = "Código de gradiente para tema visual", example = "from-purple-500 to-blue-500")
    private String gradient;
}
