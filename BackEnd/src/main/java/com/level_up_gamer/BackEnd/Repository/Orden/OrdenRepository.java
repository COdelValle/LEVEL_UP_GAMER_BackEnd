package com.level_up_gamer.BackEnd.Repository.Orden;

import org.springframework.data.jpa.repository.JpaRepository;

import com.level_up_gamer.BackEnd.Model.Orden.Orden;

public interface OrdenRepository extends JpaRepository<Orden, Long>{
    
}
