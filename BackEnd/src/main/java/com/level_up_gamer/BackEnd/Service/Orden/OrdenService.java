package com.level_up_gamer.BackEnd.Service.Orden;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.level_up_gamer.BackEnd.Model.Orden.Orden;
import com.level_up_gamer.BackEnd.Model.Orden.EstadoOrden;
import com.level_up_gamer.BackEnd.Model.Orden.OrdenItem;
import com.level_up_gamer.BackEnd.Repository.Orden.OrdenRepository;
import com.level_up_gamer.BackEnd.Repository.Producto.ProductoRepository;

@Service
public class OrdenService {
    @Autowired
    private OrdenRepository repository;

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener info:
        // Obtener todas las ordenes:
        public List<Orden> getOrdenes(){
            return repository.findAll();
        }

        // Obtener una orden por el ID:
        public Orden getOrdenByID(Long id){
            return repository.findById(id).orElse(null);
        }

        // Obtener una orden por el número(String):
        public Orden getOrdenByNumero(String numero){
            return repository.findByNumero(numero).orElse(null);
        }
    
    // Guardar(agregar o actualizar orden) orden:
    public Orden saveOrden(Orden o){
        return repository.save(o);
    }

    // Eliminar orden:
        // Por Objeto:
        public Boolean deleteOrden(Orden o){
            repository.delete(o);
            return !repository.existsById(o.getId());
        }

        // Por ID:
        public Boolean deleteOrdenById(Long id){
            repository.deleteById(id);
            return !repository.existsById(id);
        }

        // Por Número(String):
        public Boolean deleteOrdenByNumero(String numero){
            repository.deleteByNumero(numero);
            return !repository.existsByNumero(numero);
        }

    /**
     * Marca una orden como COMPLETADA y reduce el stock de los productos
     * Este método es transaccional para garantizar consistencia de datos
     */
    @Transactional
    public Orden completarOrden(Long ordenId) {
        Orden orden = repository.findById(ordenId).orElse(null);
        if (orden == null) {
            throw new IllegalArgumentException("ORDEN_NOT_FOUND::" + ordenId);
        }

        // Marcar como completada
        orden.setEstado(EstadoOrden.COMPLETADO);

        // Reducir stock de cada producto en la orden
        if (orden.getItems() != null) {
            for (OrdenItem item : orden.getItems()) {
                if (item.getProducto() != null) {
                    item.getProducto().setStock(item.getProducto().getStock() - item.getCantidad());
                    productoRepository.save(item.getProducto());
                }
            }
        }

        // Guardar la orden actualizada
        return repository.save(orden);
    }
}
