package com.level_up_gamer.BackEnd.Model.Blog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blog")
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "El título es requerido")
    @Size(min = 10, max = 200, message = "El título debe tener entre 10 y 200 caracteres")
    private String title;
    
    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    @NotBlank(message = "El resumen (excerpt) es requerido")
    @Size(min = 50, max = 500, message = "El resumen debe tener entre 50 y 500 caracteres")
    private String excerpt;
    
    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    @NotBlank(message = "El contenido es requerido")
    @Size(min = 100, message = "El contenido debe tener al menos 100 caracteres")
    private String content;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaBlog categoria;
    
    @Column(nullable = false)
    @NotBlank(message = "El autor es requerido")
    private String author;
    
    @Column(nullable = false)
    @NotNull(message = "La fecha es requerida")
    private LocalDate fecha;
    
    @Column(nullable = true)
    private String readTime;
    
    @Column(nullable = true)
    private String image;
    
    @Column(nullable = true)
    private String gradient;
    
    @Column(nullable = false)
    private Boolean featured = false;
    
    @Column(nullable = true)
    private Integer likes = 0;
    
    @ElementCollection
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
    
    @Column(nullable = true)
    private Integer views = 0;
}
