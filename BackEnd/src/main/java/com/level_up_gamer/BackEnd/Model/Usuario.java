package com.level_up_gamer.BackEnd.Model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    /*
    nombre: string;
    email: string;
    run: string;
    tipo: string;
    fechaRegistro: string;
    estado: string;
    */
    @Id
    private int id;

    private String nombre;
    private String email;
}
