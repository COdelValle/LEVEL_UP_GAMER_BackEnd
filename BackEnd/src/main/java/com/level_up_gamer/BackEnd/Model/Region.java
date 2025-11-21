package com.level_up_gamer.BackEnd.Model;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "regiones")
public class Region {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String nombre;
    
    @ElementCollection
    @Column(name = "comuna")
    private List<String> comunas = new ArrayList<>();
}