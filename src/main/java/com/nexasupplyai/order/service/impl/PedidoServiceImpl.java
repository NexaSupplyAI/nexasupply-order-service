package com.nexasupplyai.order.service.impl;

import com.nexasupplyai.order.dto.request.PedidoRequest;
import com.nexasupplyai.order.dto.response.PedidoResponse;
import com.nexasupplyai.order.entity.Pedido;
import com.nexasupplyai.order.enums.EstadoPedido;
import com.nexasupplyai.order.exception.PedidoNoEncontradoException;
import com.nexasupplyai.order.mapper.PedidoMapper;
import com.nexasupplyai.order.repository.PedidoRepository;
import com.nexasupplyai.order.service.interfaces.PedidoServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoServiceImpl implements PedidoServiceInterface {
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public List<PedidoResponse> listar(EstadoPedido estado) {
        List<Pedido> pedidos = estado == null
                ? pedidoRepository.findAll()
                : pedidoRepository.findAllByEstadoOrderBySolicitadoEnDesc(estado);
        return pedidos.stream().map(pedidoMapper::aResponse).toList();
    }

    public PedidoResponse obtenerPorId(UUID id) {
        return pedidoMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public PedidoResponse crear(PedidoRequest request) {
        validarNumeroUnico(request.numeroPedido(), null);
        validarTotal(request);
        return pedidoMapper.aResponse(pedidoRepository.save(pedidoMapper.aEntidad(request)));
    }

    @Transactional
    public PedidoResponse actualizar(UUID id, PedidoRequest request) {
        Pedido pedido = buscarEntidad(id);
        validarNumeroUnico(request.numeroPedido(), id);
        validarTotal(request);
        pedidoMapper.actualizarEntidad(request, pedido);
        return pedidoMapper.aResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public void eliminar(UUID id) {
        pedidoRepository.delete(buscarEntidad(id));
    }

    private Pedido buscarEntidad(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    private void validarNumeroUnico(String numeroPedido, UUID idActual) {
        pedidoRepository.findByNumeroPedido(numeroPedido)
                .filter(pedido -> !pedido.getId().equals(idActual))
                .ifPresent(pedido -> { throw new IllegalArgumentException("El número de pedido ya existe"); });
    }

    private void validarTotal(PedidoRequest request) {
        BigDecimal totalCalculado = request.subtotal()
                .add(request.montoImpuesto())
                .add(request.montoEnvio())
                .subtract(request.montoDescuento());
        if (request.montoDescuento().compareTo(request.subtotal().add(request.montoImpuesto())) > 0) {
            throw new IllegalArgumentException("El descuento no puede superar el subtotal más impuestos");
        }
        if (totalCalculado.compareTo(request.montoTotal()) != 0) {
            throw new IllegalArgumentException("El total no coincide con subtotal + impuestos + envío - descuento");
        }
    }
}
