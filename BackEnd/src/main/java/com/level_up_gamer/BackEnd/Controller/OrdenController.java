package com.level_up_gamer.BackEnd.Controller;

import com.level_up_gamer.BackEnd.DTO.Orden.CreateOrdenRequest;
import com.level_up_gamer.BackEnd.DTO.Orden.OrdenResponse;
import com.level_up_gamer.BackEnd.DTO.Orden.UpdateOrdenRequest;
import com.level_up_gamer.BackEnd.DTO.Orden.OrdenItemResponse;
import com.level_up_gamer.BackEnd.Model.Orden.Orden;
import com.level_up_gamer.BackEnd.Service.Orden.OrdenService;
import com.level_up_gamer.BackEnd.Exception.ResourceNotFoundException;
import com.level_up_gamer.BackEnd.Repository.Producto.ProductoRepository;
import com.level_up_gamer.BackEnd.Model.Orden.OrdenItem;
import com.level_up_gamer.BackEnd.Model.Orden.MetodoPago;
import com.level_up_gamer.BackEnd.Model.Orden.EstadoOrden;
import com.level_up_gamer.BackEnd.Model.Orden.InfoEnvio;
import com.level_up_gamer.BackEnd.Model.Producto.Producto;
import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
import com.level_up_gamer.BackEnd.Repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Órdenes", description = "API para gestión de órdenes de compra")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "apiKeyAuth")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

        // Generar número único
        orden.setNumero("ORD-" + System.currentTimeMillis());

        // Estado por defecto
        orden.setEstado(EstadoOrden.PENDIENTE);

        // Mapear metodo de pago (intento flexible)
        orden.setMetodoPago(parseMetodoPago(request.getMetodoPago()));

        // Determinar usuario (por usuarioId en request o por autenticación)
        Usuario usuario = null;
        if (request.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(request.getUsuarioId()).orElse(null);
            if (usuario == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("message", "Usuario no encontrado");
                err.put("usuarioId", request.getUsuarioId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
            }
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                Object details = auth.getDetails();
                Long idFromToken = null;
                if (details instanceof Long) idFromToken = (Long) details;
                else if (details instanceof Integer) idFromToken = Long.valueOf((Integer) details);
                if (idFromToken != null) usuario = usuarioRepository.findById(idFromToken).orElse(null);
                // If still null, try by principal (email)
                if (usuario == null && auth.getPrincipal() instanceof String) {
                    String email = (String) auth.getPrincipal();
                    usuario = usuarioRepository.findByEmail(email).orElse(null);
                }
            }
        }
        if (usuario != null) {
            orden.setUsuario(usuario);
        }

        // Mapear items y calcular total con validación de stock
        List<OrdenItem> items = request.getItems().stream().map(ir -> {
            Producto p = productoRepository.findById(ir.getProductoId()).orElse(null);
            if (p == null) {
                throw new IllegalArgumentException("PRODUCT_NOT_FOUND::" + ir.getProductoId());
            }
            if (p.getStock() < ir.getCantidad()) {
                throw new IllegalArgumentException("INSUFFICIENT_STOCK::" + ir.getProductoId());
            }
            OrdenItem oi = new OrdenItem();
            oi.setProducto(p);
            oi.setCantidad(ir.getCantidad());
            // Tomar el precio desde la entidad Producto: usar precioOferta si aplica, sino precio
            Integer unitPrice = p.getPrecio();
            if (Boolean.TRUE.equals(p.getOferta()) && p.getPrecioOferta() != null) {
                unitPrice = p.getPrecioOferta();
            }
            oi.setPrecioUnitario(unitPrice);
            return oi;
        }).collect(Collectors.toList());

        int total = items.stream().mapToInt(i -> i.getPrecioUnitario() * i.getCantidad()).sum();
        orden.setTotal(total);
        orden.setItems(items);

        // Mapear info de envío
        InfoEnvio info = new InfoEnvio();
        info.setNombre(request.getNombreEnvio());
        info.setApellido(request.getApellidoEnvio());
        info.setEmail(request.getEmail());
        info.setTelefono(request.getTelefonoEnvio());
        info.setDireccion(request.getDireccionEnvio());
        info.setDepartamento(request.getDepartamentoEnvio());
        info.setCiudad(request.getCiudadEnvio());
        info.setRegion(request.getRegionEnvio());
        info.setComuna(request.getComuna());
        info.setCodigoPostal(request.getCodigoPostal());
        info.setPais(request.getPais());
        orden.setInfoEnvio(info);

        Orden ordenGuardada = ordenService.saveOrden(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapearAResponse(ordenGuardada));
    }

    /**
     * Crea múltiples órdenes a la vez
     * Acceso: Solo ADMIN
     */
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear múltiples órdenes", description = "Crea varias órdenes en una sola solicitud")
    @ApiResponse(responseCode = "201", description = "Órdenes creadas exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "403", description = "No tiene permisos (solo ADMIN)")
    public ResponseEntity<?> crearMultiples(@Valid @RequestBody List<CreateOrdenRequest> requests) {
        List<Orden> ordenes = requests.stream().map(request -> {
            Orden orden = new Orden();

            // Generar número único
            orden.setNumero("ORD-" + System.currentTimeMillis());

            // Estado por defecto
            orden.setEstado(EstadoOrden.PENDIENTE);

            // Mapear metodo de pago
            orden.setMetodoPago(parseMetodoPago(request.getMetodoPago()));

            // Mapear items y calcular total con validación de stock
            List<OrdenItem> items = request.getItems().stream().map(ir -> {
                Producto p = productoRepository.findById(ir.getProductoId()).orElse(null);
                if (p == null) {
                    throw new IllegalArgumentException("PRODUCT_NOT_FOUND::" + ir.getProductoId());
                }
                if (p.getStock() < ir.getCantidad()) {
                    throw new IllegalArgumentException("INSUFFICIENT_STOCK::" + ir.getProductoId());
                }
                OrdenItem oi = new OrdenItem();
                oi.setProducto(p);
                oi.setCantidad(ir.getCantidad());
                Integer unitPrice = p.getPrecio();
                if (Boolean.TRUE.equals(p.getOferta()) && p.getPrecioOferta() != null) {
                    unitPrice = p.getPrecioOferta();
                }
                oi.setPrecioUnitario(unitPrice);
                return oi;
            }).collect(Collectors.toList());

            int total = items.stream().mapToInt(i -> i.getPrecioUnitario() * i.getCantidad()).sum();
            orden.setTotal(total);
            orden.setItems(items);

            // Mapear info de envío
            InfoEnvio info = new InfoEnvio();
            info.setNombre(request.getNombreEnvio());
            info.setApellido(request.getApellidoEnvio());
            info.setEmail(request.getEmail());
            info.setTelefono(request.getTelefonoEnvio());
            info.setDireccion(request.getDireccionEnvio());
            info.setDepartamento(request.getDepartamentoEnvio());
            info.setCiudad(request.getCiudadEnvio());
            info.setRegion(request.getRegionEnvio());
            info.setComuna(request.getComuna());
            info.setCodigoPostal(request.getCodigoPostal());
            info.setPais(request.getPais());
            orden.setInfoEnvio(info);

            // Determinar usuario
            Usuario usuario = null;
            if (request.getUsuarioId() != null) {
                usuario = usuarioRepository.findById(request.getUsuarioId()).orElse(null);
            } else {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    Object details = auth.getDetails();
                    Long idFromToken = null;
                    if (details instanceof Long) idFromToken = (Long) details;
                    else if (details instanceof Integer) idFromToken = Long.valueOf((Integer) details);
                    if (idFromToken != null) usuario = usuarioRepository.findById(idFromToken).orElse(null);
                    if (usuario == null && auth.getPrincipal() instanceof String) {
                        String email = (String) auth.getPrincipal();
                        usuario = usuarioRepository.findByEmail(email).orElse(null);
                    }
                }
            }
            if (usuario != null) {
                orden.setUsuario(usuario);
            }

            return orden;
        }).collect(Collectors.toList());

        List<Orden> ordenesGuardadas = ordenService.saveAllOrdenes(ordenes);
        List<OrdenResponse> response = ordenesGuardadas.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private MetodoPago parseMetodoPago(String raw) {
        if (raw == null) return MetodoPago.OTRO;
        String v = raw.trim().toUpperCase();
        if (v.contains("PAYPAL")) return MetodoPago.PAYPAL;
        if (v.contains("TRANSFER")) return MetodoPago.TRANSFERENCIA;
        if (v.contains("DEBIT")) return MetodoPago.TARJETA_DEBITO;
        if (v.contains("CREDIT")) return MetodoPago.TARJETA_CREDITO;
        if (v.contains("TARJETA") && v.contains("CREDITO")) return MetodoPago.TARJETA_CREDITO;
        if (v.contains("TARJETA") && v.contains("DEBITO")) return MetodoPago.TARJETA_DEBITO;
        if (v.contains("TARJETA")) return MetodoPago.TARJETA_CREDITO;
        return MetodoPago.OTRO;
    }

    /**
     * Actualiza una orden existente
     * Acceso: Solo ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar orden existente", description = "Actualiza el estado de una orden. Si el estado cambia a COMPLETADO, reduce automáticamente el stock de los productos")
    @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    @ApiResponse(responseCode = "403", description = "No tiene permisos (solo ADMIN)")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la orden a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrdenRequest request) {
        
        Orden orden = ordenService.getOrdenByID(id);
        if (orden == null) {
            throw new ResourceNotFoundException("Orden no encontrada");
        }
        
        // Si el nuevo estado es COMPLETADO y la orden actual no lo es, usar el método transaccional
        if (EstadoOrden.COMPLETADO.equals(request.getEstado()) && 
            !EstadoOrden.COMPLETADO.equals(orden.getEstado())) {
            orden = ordenService.completarOrden(id);
        } else {
            // Actualizar solo el estado (para otros cambios que no requieren reducción de stock)
            orden.setEstado(request.getEstado());
            orden = ordenService.saveOrden(orden);
        }
        
        return ResponseEntity.ok(mapearAResponse(orden));
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
        response.setEstado(orden.getEstado() != null ? orden.getEstado().toString() : null);
        response.setMetodoPago(orden.getMetodoPago() != null ? orden.getMetodoPago().toString() : null);
        response.setInfoEnvio(orden.getInfoEnvio());
        if (orden.getUsuario() != null) {
            response.setUsuarioId(orden.getUsuario().getId());
            response.setUsuarioNombre(orden.getUsuario().getNombre());
        }
        
        // Mapear items
        if (orden.getItems() != null) {
            response.setItems(orden.getItems().stream().map(item -> {
                OrdenItemResponse itemResponse = new OrdenItemResponse();
                itemResponse.setProductoId(item.getProducto().getId());
                itemResponse.setProductoNombre(item.getProducto().getNombre());
                itemResponse.setCantidad(item.getCantidad());
                itemResponse.setPrecioUnitario(item.getPrecioUnitario());
                itemResponse.setSubtotal(item.getPrecioUnitario() * item.getCantidad());
                return itemResponse;
            }).collect(Collectors.toList()));
        }
        
        return response;
    }
}
