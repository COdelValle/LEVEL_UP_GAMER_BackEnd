package com.level_up_gamer.BackEnd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.level_up_gamer.BackEnd.Model.Producto.Producto;

@Repository
public interface UsuarioRepository extends JpaRepository<Producto, Long>{
    
}
