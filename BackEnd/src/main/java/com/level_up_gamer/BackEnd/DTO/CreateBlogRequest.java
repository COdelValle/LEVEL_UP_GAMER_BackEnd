package com.level_up_gamer.BackEnd.DTO;

import jakarta.validation.constraints.*;
import java.util.List;

public class CreateBlogRequest {
    
    @NotBlank(message = "El título es requerido")
    @Size(min = 5, max = 200, message = "El título debe tener entre 5 y 200 caracteres")
    private String title;
    
    @NotBlank(message = "El resumen es requerido")
    @Size(min = 10, max = 500, message = "El resumen debe tener entre 10 y 500 caracteres")
    private String excerpt;
    
    @NotBlank(message = "El contenido es requerido")
    @Size(min = 50, message = "El contenido debe tener al menos 50 caracteres")
    private String content;
    
    @NotBlank(message = "La categoría es requerida")
    private String categoria;
    
    @NotBlank(message = "El autor es requerido")
    @Size(min = 2, max = 100, message = "El autor debe tener entre 2 y 100 caracteres")
    private String author;
    
    private String readTime;
    private String image;
    private String gradient;
    private Boolean featured = false;
    
    @Size(max = 10, message = "No puede haber más de 10 tags")
    private List<String> tags;
    
    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getReadTime() { return readTime; }
    public void setReadTime(String readTime) { this.readTime = readTime; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public String getGradient() { return gradient; }
    public void setGradient(String gradient) { this.gradient = gradient; }
    
    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
