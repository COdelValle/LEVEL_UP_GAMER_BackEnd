package com.level_up_gamer.BackEnd.Repository.Orden;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.level_up_gamer.BackEnd.Model.Orden.Orden;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long>{
    Optional<Orden> findByNumero(String numero);
    boolean existsByNumero(String numero);
    void deleteByNumero(String numero);
}
