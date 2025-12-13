package com.level_up_gamer.BackEnd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.level_up_gamer.BackEnd.Model.Region.RegionEntity;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, String> {
    Optional<RegionEntity> findByNombre(String nombre);
    void deleteByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
