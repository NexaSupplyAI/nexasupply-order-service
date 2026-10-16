package com.nexasupplyai.order.service.impl;

import com.nexasupplyai.order.client.InventoryServiceClient;
import com.nexasupplyai.order.dto.request.DetallePedidoRequest;
import com.nexasupplyai.order.dto.response.DetallePedidoResponse;
import com.nexasupplyai.order.dto.external.ProductoInventarioResponse;
import com.nexasupplyai.order.entity.DetallePedido;
import com.nexasupplyai.order.entity.Pedido;
import com.nexasupplyai.order.exception.DetallePedidoNoEncontradoException;
import com.nexasupplyai.order.exception.PedidoNoEncontradoException;
import com.nexasupplyai.order.mapper.DetallePedidoMapper;
import com.nexasupplyai.order.repository.DetallePedidoRepository;
import com.nexasupplyai.order.repository.PedidoRepository;
import com.nexasupplyai.order.service.interfaces.DetallePedidoServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetallePedidoServiceImpl implements DetallePedidoServiceInterface {
    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoMapper detallePedidoMapper;
    private final InventoryServiceClient inventoryServiceClient;

    public List<DetallePedidoResponse> listarPorPedido(UUID idPedido) {
        return detallePedidoRepository.findAllByPedidoId(idPedido).stream()
                .map(detallePedidoMapper::aResponse).toList();
    }

    public DetallePedidoResponse obtenerPorId(UUID id) {
        return detallePedidoMapper.aResponse(buscarEntidad(id));
    }

    @Transactional
    public DetallePedidoResponse crear(DetallePedidoRequest request) {
        validarImportes(request);
        Pedido pedido = buscarPedido(request.idPedido());
        detallePedidoRepository.findByPedidoIdAndIdProducto(pedido.getId(), request.idProducto())
                .ifPresent(detalle -> { throw new IllegalArgumentException("El producto ya existe en el pedido"); });
        return detallePedidoMapper.aResponse(
                detallePedidoRepository.save(detallePedidoMapper.aEntidad(request, pedido))
        );
    }

    @Transactional
    public DetallePedidoResponse actualizar(UUID id, DetallePedidoRequest request) {
        validarImportes(request);
        DetallePedido detalle = buscarEntidad(id);
        Pedido pedido = buscarPedido(request.idPedido());
        detallePedidoRepository.findByPedidoIdAndIdProducto(pedido.getId(), request.idProducto())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> { throw new IllegalArgumentException("El producto ya existe en el pedido"); });
        detallePedidoMapper.actualizarEntidad(request, pedido, detalle);
        return detallePedidoMapper.aResponse(detallePedidoRepository.save(detalle));
    }

    @Transactional
    public void eliminar(UUID id) {
        detallePedidoRepository.delete(buscarEntidad(id));
    }

    private void validarImportes(DetallePedidoRequest request) {
        BigDecimal importeBruto = request.cantidad().multiply(request.precioUnitario());
        if (request.montoDescuento().compareTo(importeBruto) > 0) {
            throw new IllegalArgumentException("El descuento no puede superar el importe de la línea");
        }

        ProductoInventarioResponse producto = inventoryServiceClient.obtenerProducto(request.idProducto());
        if (producto == null || !Boolean.TRUE.equals(producto.activo())) {
            throw new IllegalArgumentException("El producto no existe o está inactivo");
        }
        if (!Objects.equals(producto.sku(), request.skuInstantanea())
                || !Objects.equals(producto.nombre(), request.nombreProductoInstantanea())) {
            throw new IllegalArgumentException("El SKU o nombre del producto no coincide con Inventory Service");
        }
    }

    private DetallePedido buscarEntidad(UUID id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new DetallePedidoNoEncontradoException(id));
    }

    private Pedido buscarPedido(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }
}
