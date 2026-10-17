package com.nexasupplyai.order.exception;

import com.nexasupplyai.order.config.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({PedidoNoEncontradoException.class, DetallePedidoNoEncontradoException.class,
            HistorialEstadoPedidoNoEncontradoException.class})
    ResponseEntity<ApiError> notFound(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    ResponseEntity<ApiError> badRequest(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fields = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                error -> error.getField(),
                error -> error.getDefaultMessage() == null ? "Valor inválido" : error.getDefaultMessage(),
                (first, ignored) -> first));
        return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "La solicitud contiene campos inválidos",
                request, fields);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiError> conflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "DATA_CONFLICT", "La operación infringe una restricción de datos",
                request, Map.of());
    }

    @ExceptionHandler(RestClientException.class)
    ResponseEntity<ApiError> dependency(RestClientException ex, HttpServletRequest request) {
        log.warn("Inventory Service no disponible: {}", ex.getMessage());
        return response(HttpStatus.BAD_GATEWAY, "DEPENDENCY_UNAVAILABLE",
                "No fue posible completar la operación con Inventory Service", request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> unexpected(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado", ex);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Ocurrió un error interno",
                request, Map.of());
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String code, String message,
                                               HttpServletRequest request, Map<String, String> fields) {
        String correlationId = MDC.get(CorrelationIdFilter.MDC_KEY);
        ApiError body = new ApiError(status.value(), code, message, request.getRequestURI(), correlationId,
                OffsetDateTime.now(ZoneOffset.UTC), fields);
        return ResponseEntity.status(status).body(body);
    }
}
