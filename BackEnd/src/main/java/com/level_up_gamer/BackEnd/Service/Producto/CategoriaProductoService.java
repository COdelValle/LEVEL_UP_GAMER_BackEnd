package com.level_up_gamer.BackEnd.Service.Producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.level_up_gamer.BackEnd.Model.Producto.CategoriaProducto;
import com.level_up_gamer.BackEnd.Repository.Producto.CategoriaProductoRepository;

@Service
public class CategoriaProductoService {
    @Autowired
    private CategoriaProductoRepository repository;

    // Obtener info:
        // Obtener todas las Categorias de Productos:
        public List<CategoriaProducto> getCategoriasProductos(){
            return repository.findAll();
        }

        // Obtener un CategoriaProducto por el ID:
        public CategoriaProducto getCategoriaProductoByID(Long id){
            return repository.findById(id).orElse(null);
        }

        // Obtener un CategoriaProducto por el nombre(String):
        public CategoriaProducto getCategoriaProductoByNombre(String nombre){
            return repository.findByNombre(nombre).orElse(null);
        }
    
    // Guardar(agregar o actualizar CategoriaProducto) CategoriaProducto:
    public CategoriaProducto saveCategoriaProducto(CategoriaProducto o){
        return repository.save(o);
    }

    // Eliminar CategoriaProducto:
        // Por Objeto:
        public Boolean deleteCategoriaProducto(CategoriaProducto o){
            repository.delete(o);
            return !repository.existsById(o.getId());
        }

        // Por ID:
        public Boolean deleteCategoriaProductoById(Long id){
            repository.deleteById(id);
            return !repository.existsById(id);
        }

        // Por Número(String):
        public Boolean deleteCategoriaProductoByNombre(String nombre){
            repository.deleteByNombre(nombre);
            return !repository.existsByNombre(nombre);
        }
}
