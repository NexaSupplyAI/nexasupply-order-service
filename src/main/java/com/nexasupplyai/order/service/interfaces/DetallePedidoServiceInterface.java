package com.nexasupplyai.order.service.interfaces;

import com.nexasupplyai.order.dto.request.DetallePedidoRequest;
import com.nexasupplyai.order.dto.response.DetallePedidoResponse;

import java.util.List;
import java.util.UUID;

public interface DetallePedidoServiceInterface {
    List<DetallePedidoResponse> listarPorPedido(UUID idPedido);
    DetallePedidoResponse obtenerPorId(UUID id);
    DetallePedidoResponse crear(DetallePedidoRequest request);
    DetallePedidoResponse actualizar(UUID id, DetallePedidoRequest request);
    void eliminar(UUID id);
}
