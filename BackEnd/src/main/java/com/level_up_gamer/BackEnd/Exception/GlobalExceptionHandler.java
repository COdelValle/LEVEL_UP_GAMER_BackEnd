package com.level_up_gamer.BackEnd.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Maneja excepciones de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Error de validación");
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        response.put("errors", errors);
        return ResponseEntity.badRequest().body(response);
    }
    
    /**
     * Maneja excepciones genéricas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Maneja excepciones de recurso no encontrado
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Maneja excepciones de conflicto
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Maneja errores de negocio lanzados como IllegalArgumentException con códigos internos
     * Soporta mensajes con prefijos: PRODUCT_NOT_FOUND::id y INSUFFICIENT_STOCK::id
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage();
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());

        if (msg != null && msg.startsWith("PRODUCT_NOT_FOUND::")) {
            String id = msg.substring("PRODUCT_NOT_FOUND::".length());
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", "Producto no encontrado");
            Map<String, Object> details = new HashMap<>();
            details.put("productoId", id);
            response.put("details", details);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (msg != null && msg.startsWith("INSUFFICIENT_STOCK::")) {
            String id = msg.substring("INSUFFICIENT_STOCK::".length());
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "Stock insuficiente");
            Map<String, Object> details = new HashMap<>();
            details.put("productoId", id);
            response.put("details", details);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Fallback
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", msg != null ? msg : "Argumento inválido");
        return ResponseEntity.badRequest().body(response);
    }
}
