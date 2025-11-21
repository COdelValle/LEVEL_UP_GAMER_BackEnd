package com.level_up_gamer.BackEnd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.level_up_gamer.BackEnd.Model.Producto.Producto;

public interface UsuarioRepository extends JpaRepository<Producto, Long>{
    
}
