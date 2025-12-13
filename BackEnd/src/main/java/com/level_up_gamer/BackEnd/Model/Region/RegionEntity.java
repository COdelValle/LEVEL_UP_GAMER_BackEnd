package com.level_up_gamer.BackEnd.Model.Region;

import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "region")
public class RegionEntity {
    
    @Id
    @NotBlank(message = "El id de la región es requerido")
    private String id;
    
    @Column(nullable = false)
    @NotBlank(message = "El nombre de la región es requerido")
    private String nombre;
    
    @ElementCollection
    @Column(name = "comuna")
    private List<String> comunas = new ArrayList<>();
}