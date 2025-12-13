package com.level_up_gamer.BackEnd.Controller;

import com.level_up_gamer.BackEnd.Exception.ResourceNotFoundException;
import com.level_up_gamer.BackEnd.Model.Region.RegionEntity;
import com.level_up_gamer.BackEnd.Service.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/api/v1/regiones")
@Tag(name = "Regiones", description = "API para gestión de Regiones. GET público, POST/PUT/DELETE requiere ADMIN.")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "apiKeyAuth")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    @Operation(summary = "Obtener todas las regiones", description = "Retorna una lista de regiones. GET público.")
    @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida")
    public ResponseEntity<?> obtenerTodas() {
        List<RegionEntity> list = regionService.getRegiones();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener región por ID", description = "Retorna una región por su ID. GET público.")
    @ApiResponse(responseCode = "200", description = "Región encontrada")
    @ApiResponse(responseCode = "404", description = "No encontrado")
    public ResponseEntity<?> obtenerPorId(@PathVariable String id) {
        RegionEntity r = regionService.getRegionByID(id);
        if (r == null) throw new ResourceNotFoundException("Region con ID " + id + " no encontrada");
        return ResponseEntity.ok(r);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear región", description = "Crea una nueva región. Requiere rol ADMIN.")
    @ApiResponse(responseCode = "201", description = "Región creada")
    public ResponseEntity<?> crear(@RequestBody RegionEntity request) {
        // Ensure id is provided for Region entity (string id expected)
        if (request.getId() == null || request.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El id de la región es requerido");
        }
        RegionEntity saved = regionService.saveRegion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar región", description = "Actualiza una región existente. Requiere rol ADMIN.")
    public ResponseEntity<?> actualizar(@PathVariable String id, @RequestBody RegionEntity request) {
        RegionEntity existing = regionService.getRegionByID(id);
        if (existing == null) throw new ResourceNotFoundException("Region con ID " + id + " no encontrada");
        existing.setNombre(request.getNombre());
        existing.setComunas(request.getComunas());
        RegionEntity updated = regionService.saveRegion(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar región", description = "Elimina una región. Requiere rol ADMIN.")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        RegionEntity existing = regionService.getRegionByID(id);
        if (existing == null) throw new ResourceNotFoundException("Region con ID " + id + " no encontrada");
        regionService.deleteRegionById(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("mensaje", "Región eliminada");
        resp.put("id", id);
        return ResponseEntity.ok(resp);
    }
}
