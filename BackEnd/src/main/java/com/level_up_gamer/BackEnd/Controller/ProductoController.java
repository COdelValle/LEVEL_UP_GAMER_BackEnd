package com.level_up_gamer.BackEnd.Controller;

import com.level_up_gamer.BackEnd.DTO.Producto.CreateProductoRequest;
import com.level_up_gamer.BackEnd.DTO.Producto.ProductoResponse;
import com.level_up_gamer.BackEnd.Model.Producto.Producto;
import com.level_up_gamer.BackEnd.Model.Producto.CategoriaProducto;
import com.level_up_gamer.BackEnd.Service.Producto.CategoriaProductoService;
import com.level_up_gamer.BackEnd.Service.Producto.ProductoService;
import com.level_up_gamer.BackEnd.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para gestión del catálogo de productos. GET es público, POST/PUT/DELETE requiere autenticación.")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "apiKeyAuth")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaProductoService categoriaService;
    /**
     * Obtiene todos los productos disponibles
     * Acceso: Público (no requiere autenticación)
     */
    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos. Este endpoint es público y no requiere autenticación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente. Retorna array con todos los productos del catálogo."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al obtener productos de base de datos")
    })
    public ResponseEntity<?> obtenerTodos() {
        List<Producto> productos = productoService.getProductos();
        List<ProductoResponse> response = productos.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un producto específico por su ID
     * Acceso: Público (no requiere autenticación)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna los detalles de un producto específico. Este endpoint es público y no requiere autenticación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado. Retorna objeto completo con todos los detalles del producto."),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado. El ID proporcionado no existe en el catálogo."),
        @ApiResponse(responseCode = "400", description = "ID inválido. El parámetro id debe ser un número entero válido."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al consultar base de datos")
    })
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
     * Acceso: Solo ADMIN
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el catálogo. Acceso: requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente. Retorna objeto producto con ID generado."),
        @ApiResponse(responseCode = "400", description = "Error de validación: Nombre vacío, precio inválido, categoría no existe, stock negativo"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada. El categoriaId proporcionado no existe."),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede crear productos."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al guardar producto en base de datos")
    })
    public ResponseEntity<?> crear(@Valid @RequestBody CreateProductoRequest request) {
        
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        CategoriaProducto categoria = categoriaService.getCategoriaProductoByID(request.getCategoriaId());
        if (categoria == null) throw new ResourceNotFoundException("Categoria con ID " + request.getCategoriaId() + " no encontrada");
        producto.setCategoria(categoria);
        producto.setStock(request.getStock());
        producto.setDescripcion(request.getDescripcion());
        producto.setImagen(request.getImagen());
        producto.setDestacado(request.getDestacado() != null ? request.getDestacado() : false);
        producto.setNuevo(request.getNuevo() != null ? request.getNuevo() : false);
        producto.setOferta(request.getOferta() != null ? request.getOferta() : false);
        producto.setPrecioOferta(request.getPrecioOferta());
        if (request.getEspecificaciones() != null) {
            producto.setEspecificaciones(request.getEspecificaciones().toString());
        }
        
        Producto productoGuardado = productoService.saveProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapearAResponse(productoGuardado));
    }

    /**
     * Crea múltiples productos a la vez
     * Acceso: Solo ADMIN
     */
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear múltiples productos", description = "Crea varios productos en una sola solicitud. Acceso: requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Productos creados exitosamente. Retorna lista de productos creados."),
        @ApiResponse(responseCode = "400", description = "Error de validación: Datos inválidos, categorías no encontradas, precios inválidos"),
        @ApiResponse(responseCode = "404", description = "Una o más categorías no encontradas."),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede crear productos en lote."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al guardar productos en base de datos")
    })
    public ResponseEntity<?> crearMultiples(@Valid @RequestBody List<CreateProductoRequest> requests) {
        List<Producto> productos = requests.stream().map(request -> {
            Producto producto = new Producto();
            producto.setNombre(request.getNombre());
            producto.setPrecio(request.getPrecio());
            CategoriaProducto c = categoriaService.getCategoriaProductoByID(request.getCategoriaId());
            if (c == null) throw new ResourceNotFoundException("Categoria con ID " + request.getCategoriaId() + " no encontrada");
            producto.setCategoria(c);
            producto.setStock(request.getStock());
            producto.setDescripcion(request.getDescripcion());
            producto.setImagen(request.getImagen());
            producto.setDestacado(request.getDestacado() != null ? request.getDestacado() : false);
            producto.setNuevo(request.getNuevo() != null ? request.getNuevo() : false);
            producto.setOferta(request.getOferta() != null ? request.getOferta() : false);
            producto.setPrecioOferta(request.getPrecioOferta());
            if (request.getEspecificaciones() != null) {
                producto.setEspecificaciones(request.getEspecificaciones().toString());
            }
            return producto;
        }).collect(Collectors.toList());

        List<Producto> productosGuardados = productoService.saveAllProductos(productos);
        List<ProductoResponse> response = productosGuardados.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un producto existente
     * Acceso: Solo ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Actualizar producto existente", description = "Actualiza los detalles de un producto existente. Acceso: requiere rol ADMIN o SELLER.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente. Retorna objeto producto con datos modificados."),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado. El ID proporcionado no existe. O categoría no encontrada."),
        @ApiResponse(responseCode = "400", description = "Error de validación: Datos inválidos, precio negativo, categoría no existe, stock inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN y SELLER pueden actualizar productos."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al guardar cambios en base de datos")
    })
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
        CategoriaProducto cat = categoriaService.getCategoriaProductoByID(request.getCategoriaId());
        if (cat == null) throw new ResourceNotFoundException("Categoria con ID " + request.getCategoriaId() + " no encontrada");
        producto.setCategoria(cat);
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
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo. Acceso: requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente. Retorna confirmación con ID del producto eliminado."),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado. El ID proporcionado no existe."),
        @ApiResponse(responseCode = "400", description = "ID inválido. El parámetro id debe ser un número entero válido."),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede eliminar productos."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al eliminar producto de base de datos")
    })
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
        response.setCategoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null);
        response.setCategoriaNombre(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null);
        response.setDescripcion(producto.getDescripcion());
        response.setStock(producto.getStock());
        response.setImagen(producto.getImagen());
        response.setDestacado(producto.getDestacado());
        response.setNuevo(producto.getNuevo());
        response.setOferta(producto.getOferta());
        response.setPrecioOferta(producto.getPrecioOferta());
        response.setEspecificaciones(producto.getEspecificaciones());
        return response;
    }
}
