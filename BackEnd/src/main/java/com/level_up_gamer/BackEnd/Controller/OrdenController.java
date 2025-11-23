package com.level_up_gamer.BackEnd.Controller;

import com.level_up_gamer.BackEnd.DTO.Orden.CreateOrdenRequest;
import com.level_up_gamer.BackEnd.DTO.Orden.OrdenResponse;
import com.level_up_gamer.BackEnd.Model.Orden.Orden;
import com.level_up_gamer.BackEnd.Service.Orden.OrdenService;
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
 * Controlador para gestionar Órdenes de compra
 * 
 * @author LEVEL UP GAMER Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/ordenes")
@Tag(name = "Órdenes", description = "API para gestión de órdenes de compra")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "apiKeyAuth")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    /**
     * Obtiene todas las órdenes
     */
    @GetMapping
    @Operation(summary = "Obtener todas las órdenes", description = "Retorna una lista de todas las órdenes")
    @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida exitosamente")
    public ResponseEntity<?> obtenerTodas() {
        List<Orden> ordenes = ordenService.getOrdenes();
        List<OrdenResponse> response = ordenes.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene una orden específica por su ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Retorna los detalles de una orden específica")
    @ApiResponse(responseCode = "200", description = "Orden encontrada")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID único de la orden", required = true, example = "1")
            @PathVariable Long id) {
        
        Orden orden = ordenService.getOrdenByID(id);
        if (orden == null) {
            throw new ResourceNotFoundException("Orden con ID " + id + " no encontrada");
        }
        
        return ResponseEntity.ok(mapearAResponse(orden));
    }

    /**
     * Crea una nueva orden de compra
     * Acceso: Solo usuarios autenticados
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SELLER')")
    @Operation(summary = "Crear nueva orden", description = "Crea una nueva orden de compra")
    @ApiResponse(responseCode = "201", description = "Orden creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "403", description = "No tiene permisos")
    public ResponseEntity<?> crear(@Valid @RequestBody CreateOrdenRequest request) {
        
        Orden orden = new Orden();
        // Inicializar con datos básicos del request
        // Los detalles específicos dependen de la estructura de datos deseada
        
        Orden ordenGuardada = ordenService.saveOrden(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapearAResponse(ordenGuardada));
    }

    /**
     * Actualiza una orden existente
     * Acceso: Solo ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar orden existente", description = "Actualiza los detalles de una orden")
    @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    @ApiResponse(responseCode = "403", description = "No tiene permisos (solo ADMIN)")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la orden a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CreateOrdenRequest request) {
        
        Orden orden = ordenService.getOrdenByID(id);
        if (orden == null) {
            throw new ResourceNotFoundException("Orden no encontrada");
        }
        
        // Actualizar con datos del request
        // Los campos específicos dependen de la estructura de datos deseada
        
        Orden actualizada = ordenService.saveOrden(orden);
        return ResponseEntity.ok(mapearAResponse(actualizada));
    }

    /**
     * Elimina una orden
     * Acceso: Solo ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar orden", description = "Elimina una orden")
    @ApiResponse(responseCode = "200", description = "Orden eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    @ApiResponse(responseCode = "403", description = "No tiene permisos (solo ADMIN)")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la orden a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        
        Orden orden = ordenService.getOrdenByID(id);
        if (orden == null) {
            throw new ResourceNotFoundException("Orden no encontrada");
        }
        
        ordenService.deleteOrden(orden);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Orden eliminada exitosamente");
        response.put("id", id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Mapea una entidad Orden a su DTO de respuesta
     */
    private OrdenResponse mapearAResponse(Orden orden) {
        OrdenResponse response = new OrdenResponse();
        response.setId(orden.getId());
        response.setNumero(orden.getNumero());
        response.setFecha(orden.getFecha());
        response.setTotal(orden.getTotal());
        return response;
    }
}
