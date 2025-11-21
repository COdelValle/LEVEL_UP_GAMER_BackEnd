package com.level_up_gamer.BackEnd.Repository.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.level_up_gamer.BackEnd.Model.Producto.CategoriaProducto;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long>{
    
}
