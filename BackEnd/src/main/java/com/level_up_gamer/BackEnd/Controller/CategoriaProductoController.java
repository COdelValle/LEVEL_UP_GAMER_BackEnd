package com.level_up_gamer.BackEnd.Controller;

import com.level_up_gamer.BackEnd.Model.Producto.CategoriaProducto;
import com.level_up_gamer.BackEnd.Service.Producto.CategoriaProductoService;
import com.level_up_gamer.BackEnd.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias", description = "API para gestión de categorías de productos. GET público, POST/PUT/DELETE requiere ADMIN.")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "apiKeyAuth")
public class CategoriaProductoController {

    @Autowired
    private CategoriaProductoService categoriaService;

    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Retorna una lista de categorías. GET público.")
    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida")
    public ResponseEntity<?> obtenerTodas() {
        List<CategoriaProducto> list = categoriaService.getCategoriasProductos();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Retorna una categoría por su ID. GET público.")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada")
    @ApiResponse(responseCode = "404", description = "No encontrado")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        CategoriaProducto c = categoriaService.getCategoriaProductoByID(id);
        if (c == null) throw new ResourceNotFoundException("Categoria con ID " + id + " no encontrada");
        return ResponseEntity.ok(c);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría. Requiere rol ADMIN.")
    @ApiResponse(responseCode = "201", description = "Categoría creada")
    public ResponseEntity<?> crear(@RequestBody CategoriaProducto request) {
        CategoriaProducto saved = categoriaService.saveCategoriaProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear múltiples categorías", description = "Crea varias categorías en una sola solicitud. Requiere rol ADMIN.")
    @ApiResponse(responseCode = "201", description = "Categorías creadas")
    public ResponseEntity<?> crearMultiples(@RequestBody List<CategoriaProducto> requests) {
        List<CategoriaProducto> categoriasGuardadas = categoriaService.saveAllCategoriasProducto(requests);
        Map<String, Object> resp = new HashMap<>();
        resp.put("mensaje", "Categorías creadas exitosamente");
        resp.put("cantidad", categoriasGuardadas.size());
        resp.put("categorias", categoriasGuardadas);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CategoriaProducto request) {
        CategoriaProducto existing = categoriaService.getCategoriaProductoByID(id);
        if (existing == null) throw new ResourceNotFoundException("Categoria con ID " + id + " no encontrada");
        existing.setNombre(request.getNombre());
        existing.setSlug(request.getSlug());
        existing.setDescripcion(request.getDescripcion());
        existing.setIcono(request.getIcono());
        CategoriaProducto updated = categoriaService.saveCategoriaProducto(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría eliminada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        CategoriaProducto existing = categoriaService.getCategoriaProductoByID(id);
        if (existing == null) throw new ResourceNotFoundException("Categoria con ID " + id + " no encontrada");
        categoriaService.deleteCategoriaProductoById(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("mensaje", "Categoría eliminada");
        resp.put("id", id);
        return ResponseEntity.ok(resp);
    }
}
