package com.nexasupplyai.order.exception;

import java.util.UUID;

public class DetallePedidoNoEncontradoException extends RuntimeException {
    public DetallePedidoNoEncontradoException(UUID id) {
        super("Detalle de pedido no encontrado: " + id);
    }
}
