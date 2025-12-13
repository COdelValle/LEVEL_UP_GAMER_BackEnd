package com.level_up_gamer.BackEnd.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.level_up_gamer.BackEnd.Model.Region.RegionEntity;
import com.level_up_gamer.BackEnd.Repository.RegionRepository;

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
}
