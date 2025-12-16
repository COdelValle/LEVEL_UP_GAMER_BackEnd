package com.level_up_gamer.BackEnd.Controller;

import com.level_up_gamer.BackEnd.Exception.ResourceNotFoundException;
import com.level_up_gamer.BackEnd.Model.Region.RegionEntity;
import com.level_up_gamer.BackEnd.Service.RegionService;

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
@RequestMapping("/api/v1/regiones")
@Tag(name = "Regiones", description = "API para gestión de Regiones. GET público, POST/PUT/DELETE requiere ADMIN.")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "apiKeyAuth")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    @Operation(summary = "Obtener todas las regiones", description = "Retorna una lista de regiones. GET público.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida exitosamente. Retorna array con todas las regiones disponibles."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos")
    })
    public ResponseEntity<?> obtenerTodas() {
        List<RegionEntity> list = regionService.getRegiones();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener región por ID", description = "Retorna una región por su ID. GET público.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región encontrada. Retorna objeto con detalles de la región."),
        @ApiResponse(responseCode = "404", description = "Región no encontrada. El ID proporcionado no existe."),
        @ApiResponse(responseCode = "400", description = "ID inválido. El parámetro id debe ser una cadena válida."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos")
    })
    public ResponseEntity<?> obtenerPorId(@PathVariable String id) {
        RegionEntity r = regionService.getRegionByID(id);
        if (r == null) throw new ResourceNotFoundException("Region con ID " + id + " no encontrada");
        return ResponseEntity.ok(r);
    }

    @GetMapping("/{id}/comunas")
    @Operation(summary = "Obtener comunas de una región", description = "Retorna las comunas pertenecientes a una región. GET público.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comunas obtenidas exitosamente. Retorna array con todas las comunas de la región."),
        @ApiResponse(responseCode = "404", description = "Región no encontrada. El ID proporcionado no existe."),
        @ApiResponse(responseCode = "400", description = "ID inválido. El parámetro id debe ser una cadena válida."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos")
    })
    public ResponseEntity<?> obtenerComunasPorRegion(@PathVariable String id) {
        RegionEntity r = regionService.getRegionByID(id);
        if (r == null) throw new ResourceNotFoundException("Region con ID " + id + " no encontrada");
        return ResponseEntity.ok(regionService.getComunasByRegion(id));
    }

    @GetMapping("/comunas")
    @Operation(summary = "Obtener todas las comunas", description = "Retorna la lista de todas las comunas registradas en el catálogo. GET público.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de comunas obtenida exitosamente. Retorna array con todas las comunas del sistema."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos")
    })
    public ResponseEntity<?> obtenerTodasComunas() {
        return ResponseEntity.ok(regionService.getAllComunas());
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar región por ciudad/comuna", description = "Busca la región que contiene la ciudad/comuna dada. GET público.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada. Retorna región si existe, o null si no se encuentra."),
        @ApiResponse(responseCode = "400", description = "Parámetro city vacío o inválido."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos")
    })
    public ResponseEntity<?> buscarRegionPorCiudad(@RequestParam String city) {
        RegionEntity r = regionService.getRegionByCity(city);
        if (r == null) return ResponseEntity.ok().body(null);
        return ResponseEntity.ok(r);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar comuna en región", description = "Valida si una comuna pertenece a una región específica. GET público.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validación completada. Retorna objeto con resultado (valid: true/false)."),
        @ApiResponse(responseCode = "400", description = "Parámetros regionId o comuna vacíos o inválidos."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos")
    })
    public ResponseEntity<?> validarComunaEnRegion(@RequestParam String regionId, @RequestParam String comuna) {
        boolean ok = regionService.isValidComunaForRegion(regionId, comuna);
        Map<String, Object> resp = new HashMap<>();
        resp.put("regionId", regionId);
        resp.put("comuna", comuna);
        resp.put("valid", ok);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/validate-city")
    @Operation(summary = "Validar existencia de ciudad", description = "Valida si una ciudad/comuna existe en el catálogo. GET público.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validación completada. Retorna objeto con resultado (valid: true/false)."),
        @ApiResponse(responseCode = "400", description = "Parámetro city vacío o inválido."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo en base de datos")
    })
    public ResponseEntity<?> validarCiudad(@RequestParam String city) {
        boolean ok = regionService.isCityValid(city);
        Map<String, Object> resp = new HashMap<>();
        resp.put("city", city);
        resp.put("valid", ok);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear región", description = "Crea una nueva región. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Región creada exitosamente. Retorna objeto región con ID asignado."),
        @ApiResponse(responseCode = "400", description = "Error de validación: ID faltante o vacío, nombre vacío, datos requeridos faltantes"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede crear regiones."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al guardar región en base de datos")
    })
    public ResponseEntity<?> crear(@RequestBody RegionEntity request) {
        // Ensure id is provided for Region entity (string id expected)
        if (request.getId() == null || request.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El id de la región es requerido");
        }
        RegionEntity saved = regionService.saveRegion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear múltiples regiones", description = "Crea varias regiones en una sola petición. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Regiones creadas exitosamente. Retorna lista de regiones creadas."),
        @ApiResponse(responseCode = "400", description = "Error de validación: IDs faltantes, nombres vacíos, datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede crear regiones en lote."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al guardar regiones en base de datos")
    })
    public ResponseEntity<?> crearMultiples(@RequestBody List<RegionEntity> requests) {
        List<RegionEntity> saved = regionService.saveAllRegiones(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar región", description = "Actualiza una región existente. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región actualizada exitosamente. Retorna objeto con nuevos datos."),
        @ApiResponse(responseCode = "404", description = "Región no encontrada. El ID proporcionado no existe."),
        @ApiResponse(responseCode = "400", description = "Error de validación: Datos inválidos, ID inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede actualizar regiones."),
        @ApiResponse(responseCode = "422", description = "Datos no procesables. Validación fallida."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al guardar cambios en base de datos")
    })
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región eliminada exitosamente. Retorna confirmación con ID eliminado."),
        @ApiResponse(responseCode = "404", description = "Región no encontrada. El ID proporcionado no existe."),
        @ApiResponse(responseCode = "400", description = "ID inválido. El parámetro id debe ser una cadena válida."),
        @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT inválido, expirado o ausente."),
        @ApiResponse(responseCode = "403", description = "No autorizado. Solo ADMIN puede eliminar regiones."),
        @ApiResponse(responseCode = "500", description = "Error interno: fallo al eliminar región de base de datos")
    })
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

