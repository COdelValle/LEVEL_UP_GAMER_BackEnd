package com.level_up_gamer.BackEnd.Model.Orden;

import java.time.LocalDateTime;
import java.util.List;

import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "El número de orden es requerido")
    private String numero;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
    
    @Column(nullable = false)
    @NotNull(message = "El total es requerido")
    @Min(value = 0, message = "El total no puede ser negativo")
    private Integer total;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado es requerido")
    private EstadoOrden estado;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El método de pago es requerido")
    private MetodoPago metodoPago;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orden_id", nullable = false)
    @NotNull(message = "Los items de la orden son requeridos")
    @Size(min = 1, message = "La orden debe contener al menos un item")
    private List<OrdenItem> items;
    
    @Embedded
    @NotNull(message = "La información de envío es requerida")
    private InfoEnvio infoEnvio;
}