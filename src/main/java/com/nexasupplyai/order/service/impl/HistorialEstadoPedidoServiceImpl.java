package com.nexasupplyai.order.service.impl;

import com.nexasupplyai.order.dto.request.HistorialEstadoPedidoRequest;
import com.nexasupplyai.order.dto.response.HistorialEstadoPedidoResponse;
import com.nexasupplyai.order.entity.HistorialEstadoPedido;
import com.nexasupplyai.order.entity.Pedido;
import com.nexasupplyai.order.exception.HistorialEstadoPedidoNoEncontradoException;
import com.nexasupplyai.order.exception.PedidoNoEncontradoException;
import com.nexasupplyai.order.mapper.HistorialEstadoPedidoMapper;
import com.nexasupplyai.order.repository.HistorialEstadoPedidoRepository;
import com.nexasupplyai.order.repository.PedidoRepository;
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
    private final PedidoRepository pedidoRepository;
    private final HistorialEstadoPedidoMapper historialMapper;

    public List<HistorialEstadoPedidoResponse> listarPorPedido(UUID idPedido) {
        return historialRepository.findAllByPedidoIdOrderByCambiadoEnDesc(idPedido).stream()
                .map(historialMapper::aResponse).toList();
    }

    public HistorialEstadoPedidoResponse obtenerPorId(UUID id) {
        return historialMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public HistorialEstadoPedidoResponse crear(HistorialEstadoPedidoRequest request) {
        return historialMapper.aResponse(historialRepository.save(
                historialMapper.aEntidad(request, buscarPedido(request.idPedido()))
        ));
    }

    @Transactional
    public HistorialEstadoPedidoResponse actualizar(UUID id, HistorialEstadoPedidoRequest request) {
        HistorialEstadoPedido historial = buscarEntidad(id);
        historialMapper.actualizarEntidad(request, buscarPedido(request.idPedido()), historial);
        return historialMapper.aResponse(historialRepository.save(historial));
    }

    @Transactional
    public void eliminar(UUID id) {
        historialRepository.delete(buscarEntidad(id));
    }

    private HistorialEstadoPedido buscarEntidad(UUID id) {
        return historialRepository.findById(id)
                .orElseThrow(() -> new HistorialEstadoPedidoNoEncontradoException(id));
    }

    private Pedido buscarPedido(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }
}
