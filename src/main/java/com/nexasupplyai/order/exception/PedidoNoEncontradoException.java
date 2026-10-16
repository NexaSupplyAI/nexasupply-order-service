package com.nexasupplyai.order.exception;

import java.util.UUID;

public class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException(UUID id) {
        super("Pedido no encontrado: " + id);
    }
}
