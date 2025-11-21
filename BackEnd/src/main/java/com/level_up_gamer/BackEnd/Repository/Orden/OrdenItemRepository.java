package com.level_up_gamer.BackEnd.Repository.Orden;

import org.springframework.data.jpa.repository.JpaRepository;

import com.level_up_gamer.BackEnd.Model.Orden.OrdenItem;

public interface OrdenItemRepository extends JpaRepository<OrdenItem, Long> {
    
}
