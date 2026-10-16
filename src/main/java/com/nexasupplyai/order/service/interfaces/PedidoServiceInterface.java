package com.nexasupplyai.order.service.interfaces;

import com.nexasupplyai.order.dto.request.PedidoRequest;
import com.nexasupplyai.order.dto.response.PedidoResponse;
import com.nexasupplyai.order.enums.EstadoPedido;

import java.util.List;
import java.util.UUID;

public interface PedidoServiceInterface {
    List<PedidoResponse> listar(EstadoPedido estado);
    PedidoResponse obtenerPorId(UUID id);
    PedidoResponse crear(PedidoRequest request);
    PedidoResponse actualizar(UUID id, PedidoRequest request);
    void eliminar(UUID id);
}
