package com.level_up_gamer.BackEnd.Service.Producto;

import java.util.List;
import java.util.Map;

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

    // Guardar múltiples productos:
    public List<Producto> saveAllProductos(List<Producto> productos){
        // Evitar violaciones de clave única por nombre:
        // - Si existe un producto en BD con el mismo nombre, lo actualizamos.
        // - Si la lista de entrada contiene duplicados por nombre, los fusionamos en un único objeto.
        Map<String, Producto> pendientes = new java.util.LinkedHashMap<>();
        for (Producto p : productos) {
            String nombre = p.getNombre() != null ? p.getNombre().trim() : null;
            if (nombre == null) continue;

            if (pendientes.containsKey(nombre)) {
                // fusionar campos no nulos/útiles del nuevo objeto en el existente
                Producto existente = pendientes.get(nombre);
                mergeProductoFields(existente, p);
                pendientes.put(nombre, existente);
            } else {
                // comprobar si ya existe en la BD
                Producto existInDb = repository.findByNombre(nombre).orElse(null);
                if (existInDb != null) {
                    mergeProductoFields(existInDb, p);
                    pendientes.put(nombre, existInDb);
                } else {
                    // nuevo producto, asegurarse de limpiar id en caso de venir seteo
                    p.setId(null);
                    pendientes.put(nombre, p);
                }
            }
        }

        List<Producto> listaAGuardar = new java.util.ArrayList<>(pendientes.values());
        return repository.saveAll(listaAGuardar);
    }

    private void mergeProductoFields(Producto target, Producto src) {
        if (src.getPrecio() != null) target.setPrecio(src.getPrecio());
        if (src.getCategoria() != null) target.setCategoria(src.getCategoria());
        if (src.getDescripcion() != null) target.setDescripcion(src.getDescripcion());
        if (src.getImagen() != null) target.setImagen(src.getImagen());
        if (src.getStock() != null) target.setStock(src.getStock());
        if (src.getDestacado() != null) target.setDestacado(src.getDestacado());
        if (src.getNuevo() != null) target.setNuevo(src.getNuevo());
        if (src.getOferta() != null) target.setOferta(src.getOferta());
        if (src.getPrecioOferta() != null) target.setPrecioOferta(src.getPrecioOferta());
        if (src.getEspecificaciones() != null) target.setEspecificaciones(src.getEspecificaciones());
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
