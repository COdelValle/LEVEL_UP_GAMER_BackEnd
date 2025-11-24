package com.level_up_gamer.BackEnd.Controller;

import com.level_up_gamer.BackEnd.DTO.Producto.CreateProductoRequest;
import com.level_up_gamer.BackEnd.DTO.Producto.ProductoResponse;
import com.level_up_gamer.BackEnd.Model.Producto.Producto;
import com.level_up_gamer.BackEnd.Service.Producto.ProductoService;
import com.level_up_gamer.BackEnd.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para gestionar Productos
 * 
 * @author LEVEL UP GAMER Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestión del catálogo de productos")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "apiKeyAuth")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /**
     * Obtiene todos los productos disponibles
     */
    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    public ResponseEntity<?> obtenerTodos() {
        List<Producto> productos = productoService.getProductos();
        List<ProductoResponse> response = productos.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un producto específico por su ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna los detalles de un producto específico")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID único del producto", required = true, example = "1")
            @PathVariable Long id) {
        
        Producto producto = productoService.getProductoByID(id);
        if (producto == null) {
            throw new ResourceNotFoundException("Producto con ID " + id + " no encontrado");
        }
        
        return ResponseEntity.ok(mapearAResponse(producto));
    }

    /**
     * Crea un nuevo producto en el catálogo
     * Acceso: Solo ADMIN y SELLER
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "403", description = "No tiene permisos")
    public ResponseEntity<?> crear(@Valid @RequestBody CreateProductoRequest request) {
        
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(request.getCategoria());
        producto.setStock(request.getStock());
        producto.setDescripcion(request.getDescripcion());
        producto.setImagen(request.getImagen());
        producto.setDestacado(request.getDestacado() != null ? request.getDestacado() : false);
        producto.setNuevo(request.getNuevo() != null ? request.getNuevo() : false);
        
        Producto productoGuardado = productoService.saveProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapearAResponse(productoGuardado));
    }

    /**
     * Actualiza un producto existente
     * Acceso: Solo ADMIN y SELLER
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Actualizar producto existente", description = "Actualiza los detalles de un producto")
    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "403", description = "No tiene permisos")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CreateProductoRequest request) {
        
        Producto producto = productoService.getProductoByID(id);
        if (producto == null) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(request.getCategoria());
        producto.setStock(request.getStock());
        producto.setDescripcion(request.getDescripcion());
        producto.setImagen(request.getImagen());
        producto.setDestacado(request.getDestacado());
        producto.setNuevo(request.getNuevo());
        
        Producto actualizado = productoService.saveProducto(producto);
        return ResponseEntity.ok(mapearAResponse(actualizado));
    }

    /**
     * Elimina un producto del catálogo
     * Acceso: Solo ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo")
    @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "403", description = "No tiene permisos (solo ADMIN)")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        
        Producto producto = productoService.getProductoByID(id);
        if (producto == null) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        
        productoService.deleteProducto(producto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Producto eliminado exitosamente");
        response.put("id", id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Mapea una entidad Producto a su DTO de respuesta
     */
    private ProductoResponse mapearAResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setPrecio(producto.getPrecio());
        response.setCategoria(producto.getCategoria());
        response.setDescripcion(producto.getDescripcion());
        response.setStock(producto.getStock());
        response.setImagen(producto.getImagen());
        response.setDestacado(producto.getDestacado());
        response.setNuevo(producto.getNuevo());
        response.setOferta(producto.getOferta());
        response.setPrecioOferta(producto.getPrecioOferta());
        return response;
    }
}
