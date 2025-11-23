package com.level_up_gamer.BackEnd.Service.Orden;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.level_up_gamer.BackEnd.Model.Orden.OrdenItem;
import com.level_up_gamer.BackEnd.Repository.Orden.OrdenItemRepository;

@Service
public class OrdenItemService {
    @Autowired
    private OrdenItemRepository repository;

    // Obtener info:
        // Obtener todos los items de las ordenes:
        public List<OrdenItem> getItemsOrdenes(){
            return repository.findAll();
        }

        // Obtener un item de orden por el ID:
        public OrdenItem getOrdenItemByID(Long id){
            return repository.findById(id).orElse(null);
        }
    
    // Guardar(agregar o actualizar item) item:
    public OrdenItem saveOrdenItem(OrdenItem o){
        return repository.save(o);
    }

    // Eliminar item:
        // Por Objeto:
        public Boolean deleteOrdenItem(OrdenItem o){
            repository.delete(o);
            return !repository.existsById(o.getId());
        }

        // Por ID:
        public Boolean deleteOrdenItemById(Long id){
            repository.deleteById(id);
            return !repository.existsById(id);
        }
}
