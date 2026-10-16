package com.nexasupplyai.order.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            PedidoNoEncontradoException.class,
            DetallePedidoNoEncontradoException.class,
            HistorialEstadoPedidoNoEncontradoException.class
    })
    ProblemDetail manejarNoEncontrado(RuntimeException exception) {
        return problema(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail manejarSolicitudInvalida(IllegalArgumentException exception) {
        return problema(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail manejarValidacion(MethodArgumentNotValidException exception) {
        ProblemDetail problema = problema(HttpStatus.BAD_REQUEST, "La solicitud contiene campos inválidos");
        Map<String, String> errores = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage() == null ? "Valor inválido" : error.getDefaultMessage(),
                        (primero, segundo) -> primero
                ));
        problema.setProperty("errores", errores);
        return problema;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail manejarIntegridad(DataIntegrityViolationException exception) {
        return problema(HttpStatus.CONFLICT, "La operación infringe una restricción de datos");
    }

    @ExceptionHandler(RestClientException.class)
    ProblemDetail manejarDependenciaNoDisponible(RestClientException exception) {
        return problema(HttpStatus.BAD_GATEWAY, "No fue posible consultar Inventory Service");
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail manejarErrorInesperado(Exception exception) {
        return problema(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno");
    }

    private ProblemDetail problema(HttpStatus estado, String detalle) {
        return ProblemDetail.forStatusAndDetail(estado, detalle);
    }
}
