package com.level_up_gamer.BackEnd.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    
    private String token;
    private String apiKey;
    private Long usuarioId;
    private String nombre;
    private String email;
    private String rol;
}
