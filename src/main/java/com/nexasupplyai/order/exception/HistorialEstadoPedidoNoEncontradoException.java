package com.nexasupplyai.order.exception;

import java.util.UUID;

public class HistorialEstadoPedidoNoEncontradoException extends RuntimeException {
    public HistorialEstadoPedidoNoEncontradoException(UUID id) {
        super("Historial de estado no encontrado: " + id);
    }
}
