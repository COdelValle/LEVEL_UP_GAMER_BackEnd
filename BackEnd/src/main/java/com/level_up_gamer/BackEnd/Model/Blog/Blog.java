package com.level_up_gamer.BackEnd.Model.Blog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articulos_blog")
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String excerpt;
    
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaBlog categoria;
    
    @Column(nullable = false)
    private String author;
    
    @Column(nullable = false)
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
