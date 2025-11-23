package com.level_up_gamer.BackEnd.DTO.Blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * DTO para respuesta de artículo de blog
 * Usado en respuestas de GET y POST de blog
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información detallada de un artículo de blog")
public class BlogResponse {
    
    @Schema(description = "ID único del artículo", example = "1")
    private Long id;
    
    @Schema(description = "Título del artículo", example = "Guía Completa: Optimizar tu PC para Gaming")
    private String title;
    
    @Schema(description = "Resumen del artículo", example = "Aprende los mejores trucos para maximizar...")
    private String excerpt;
    
    @Schema(description = "Contenido completo del artículo")
    private String content;
    
    @Schema(description = "Categoría del artículo", example = "Gaming")
    private String categoria;
    
    @Schema(description = "Autor del artículo", example = "Juan García")
    private String author;
    
    @Schema(description = "Fecha de publicación", example = "2025-11-23")
    private LocalDate fecha;
    
    @Schema(description = "Tiempo estimado de lectura", example = "5 minutos")
    private String readTime;
    
    @Schema(description = "URL de la imagen de portada")
    private String image;
    
    @Schema(description = "Código de gradiente para tema visual")
    private String gradient;
}
