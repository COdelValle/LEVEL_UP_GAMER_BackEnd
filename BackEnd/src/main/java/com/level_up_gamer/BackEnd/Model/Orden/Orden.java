package com.level_up_gamer.BackEnd.Model.Orden;

import java.time.LocalDateTime;
import java.util.List;

import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orden")
public class Orden {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String numero;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
    
    @Column(nullable = false)
    private Integer total;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orden_id", nullable = false)
    private List<OrdenItem> items;
    
    @Embedded
    private InfoEnvio infoEnvio;
}