package com.level_up_gamer.BackEnd.Service.Producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.level_up_gamer.BackEnd.Model.Producto.Producto;
import com.level_up_gamer.BackEnd.Repository.Producto.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repository;

    // Obtener info:
        // Obtener todas los productos:
        public List<Producto> getProductos(){
            return repository.findAll();
        }

        // Obtener un producto por el ID:
        public Producto getProductoByID(Long id){
            return repository.findById(id).orElse(null);
        }

        // Obtener un Producto por el nombre(String):
        public Producto getProductoByNombre(String nombre){
            return repository.findByNombre(nombre).orElse(null);
        }
    
    // Guardar(agregar o actualizar Producto) Producto:
    public Producto saveProducto(Producto o){
        return repository.save(o);
    }

    // Eliminar Producto:
        // Por Objeto:
        public Boolean deleteProducto(Producto o){
            repository.delete(o);
            return !repository.existsById(o.getId());
        }

        // Por ID:
        public Boolean deleteProductoById(Long id){
            repository.deleteById(id);
            return !repository.existsById(id);
        }

        // Por Número(String):
        public Boolean deleteProductoByNombre(String nombre){
            repository.deleteByNombre(nombre);
            return !repository.existsByNombre(nombre);
        }
}
