package com.nexasupplyai.order.service.interfaces;

import com.nexasupplyai.order.dto.response.HistorialEstadoPedidoResponse;

import java.util.List;
import java.util.UUID;

public interface HistorialEstadoPedidoServiceInterface {
    List<HistorialEstadoPedidoResponse> listarPorPedido(UUID idPedido);
    HistorialEstadoPedidoResponse obtenerPorId(UUID id);
}
