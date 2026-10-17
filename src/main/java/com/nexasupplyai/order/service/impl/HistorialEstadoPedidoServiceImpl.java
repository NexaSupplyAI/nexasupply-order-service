package com.nexasupplyai.order.service.impl;

import com.nexasupplyai.order.dto.response.HistorialEstadoPedidoResponse;
import com.nexasupplyai.order.entity.HistorialEstadoPedido;
import com.nexasupplyai.order.exception.HistorialEstadoPedidoNoEncontradoException;
import com.nexasupplyai.order.mapper.HistorialEstadoPedidoMapper;
import com.nexasupplyai.order.repository.HistorialEstadoPedidoRepository;
import com.nexasupplyai.order.service.interfaces.HistorialEstadoPedidoServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistorialEstadoPedidoServiceImpl implements HistorialEstadoPedidoServiceInterface {
    private final HistorialEstadoPedidoRepository historialRepository;
    private final HistorialEstadoPedidoMapper historialMapper;

    public List<HistorialEstadoPedidoResponse> listarPorPedido(UUID idPedido) {
        return historialRepository.findAllByPedidoIdOrderByCambiadoEnDesc(idPedido).stream()
                .map(historialMapper::aResponse).toList();
    }

    public HistorialEstadoPedidoResponse obtenerPorId(UUID id) {
        return historialMapper.aResponse(buscarEntidad(id));
    }

    private HistorialEstadoPedido buscarEntidad(UUID id) {
        return historialRepository.findById(id)
                .orElseThrow(() -> new HistorialEstadoPedidoNoEncontradoException(id));
    }
}
