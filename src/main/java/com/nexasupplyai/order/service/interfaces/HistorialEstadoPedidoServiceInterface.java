package com.nexasupplyai.order.service.interfaces;

import com.nexasupplyai.order.dto.request.HistorialEstadoPedidoRequest;
import com.nexasupplyai.order.dto.response.HistorialEstadoPedidoResponse;

import java.util.List;
import java.util.UUID;

public interface HistorialEstadoPedidoServiceInterface {
    List<HistorialEstadoPedidoResponse> listarPorPedido(UUID idPedido);
    HistorialEstadoPedidoResponse obtenerPorId(UUID id);
    HistorialEstadoPedidoResponse crear(HistorialEstadoPedidoRequest request);
    HistorialEstadoPedidoResponse actualizar(UUID id, HistorialEstadoPedidoRequest request);
    void eliminar(UUID id);
}
