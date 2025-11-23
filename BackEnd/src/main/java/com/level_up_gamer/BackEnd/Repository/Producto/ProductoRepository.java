package com.level_up_gamer.BackEnd.Repository.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.level_up_gamer.BackEnd.Model.Producto.Producto;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    Optional<Producto> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    void deleteByNombre(String nombre);
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByOfertaTrue();
    List<Producto> findByDestacadoTrue();
}
