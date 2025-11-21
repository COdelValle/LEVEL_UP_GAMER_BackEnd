package com.level_up_gamer.BackEnd.Repository.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

import com.level_up_gamer.BackEnd.Model.Producto.CategoriaProducto;

public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long>{
    
}
