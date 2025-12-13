package com.level_up_gamer.BackEnd.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.level_up_gamer.BackEnd.Model.Region.RegionEntity;
import com.level_up_gamer.BackEnd.Repository.RegionRepository;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository repository;

    public List<RegionEntity> getRegiones(){
        return repository.findAll();
    }

    public RegionEntity getRegionByID(String id){
        return repository.findById(id).orElse(null);
    }

    public RegionEntity getRegionByNombre(String nombre){
        return repository.findByNombre(nombre).orElse(null);
    }

    public RegionEntity saveRegion(RegionEntity o){
        return repository.save(o);
    }

    public List<RegionEntity> saveAllRegiones(List<RegionEntity> regiones){
        return repository.saveAll(regiones);
    }

    public Boolean deleteRegion(RegionEntity o){
        repository.delete(o);
        return !repository.existsById(o.getId());
    }

    public Boolean deleteRegionById(String id){
        repository.deleteById(id);
        return !repository.existsById(id);
    }

    public Boolean deleteRegionByNombre(String nombre){
        repository.deleteByNombre(nombre);
        return !repository.existsByNombre(nombre);
    }

    // Obtener comunas por id de región
    public List<String> getComunasByRegion(String regionId) {
        RegionEntity r = getRegionByID(regionId);
        return r == null ? Collections.emptyList() : r.getComunas();
    }

    // Obtener todas las comunas del catálogo (sin duplicados)
    public List<String> getAllComunas() {
        return repository.findAll().stream()
            .flatMap(reg -> reg.getComunas().stream())
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .distinct()
            .collect(Collectors.toList());
    }

    // Validar si una comuna pertenece a una región
    public boolean isValidComunaForRegion(String regionId, String comuna) {
        if (comuna == null) return false;
        return getComunasByRegion(regionId).stream()
            .anyMatch(c -> c.equalsIgnoreCase(comuna.trim()));
    }

    // Obtener región por ciudad/comuna (busca la primera coincidencia)
    public RegionEntity getRegionByCity(String cityName) {
        if (cityName == null) return null;
        String q = cityName.trim();
        Optional<RegionEntity> found = repository.findAll().stream()
            .filter(r -> r.getComunas().stream().anyMatch(c -> c.equalsIgnoreCase(q)))
            .findFirst();
        return found.orElse(null);
    }

    // Validar si una ciudad existe en cualquier región
    public boolean isCityValid(String cityName) {
        if (cityName == null) return false;
        String q = cityName.trim();
        return getAllComunas().stream().anyMatch(c -> c.equalsIgnoreCase(q));
    }
}
