package com.nexasupplyai.order.service.impl;

import com.nexasupplyai.order.client.InventoryServiceClient;
import com.nexasupplyai.order.dto.external.LineaReservaInventarioRequest;
import com.nexasupplyai.order.dto.external.ReservaOrdenInventarioRequest;
import com.nexasupplyai.order.dto.request.PedidoRequest;
import com.nexasupplyai.order.dto.response.PedidoResponse;
import com.nexasupplyai.order.entity.Pedido;
import com.nexasupplyai.order.entity.DetallePedido;
import com.nexasupplyai.order.entity.HistorialEstadoPedido;
import com.nexasupplyai.order.enums.EstadoPedido;
import com.nexasupplyai.order.exception.PedidoNoEncontradoException;
import com.nexasupplyai.order.mapper.PedidoMapper;
import com.nexasupplyai.order.repository.PedidoRepository;
import com.nexasupplyai.order.repository.DetallePedidoRepository;
import com.nexasupplyai.order.repository.HistorialEstadoPedidoRepository;
import com.nexasupplyai.order.service.interfaces.PedidoServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoServiceImpl implements PedidoServiceInterface {
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final DetallePedidoRepository detalleRepository;
    private final HistorialEstadoPedidoRepository historialRepository;
    private final InventoryServiceClient inventoryServiceClient;

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
        Pedido pedido = pedidoMapper.aEntidad(request);
        pedido.setEstado(EstadoPedido.PENDING);
        return pedidoMapper.aResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse actualizar(UUID id, PedidoRequest request) {
        Pedido pedido = buscarEntidad(id);
        validarNumeroUnico(request.numeroPedido(), id);
        validarTotal(request);
        validarEditable(pedido);
        EstadoPedido estado = pedido.getEstado();
        pedidoMapper.actualizarEntidad(request, pedido);
        pedido.setEstado(estado);
        return pedidoMapper.aResponse(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional
    public PedidoResponse confirmar(UUID id, String actor) {
        Pedido pedido = bloquear(id);
        if (pedido.getEstado() == EstadoPedido.CONFIRMED) return pedidoMapper.aResponse(pedido);
        if (pedido.getEstado() != EstadoPedido.PENDING) {
            throw new IllegalArgumentException("Solo se puede confirmar un pedido pendiente");
        }
        List<DetallePedido> detalles = detalleRepository.findAllByPedidoId(id);
        if (detalles.isEmpty()) throw new IllegalArgumentException("El pedido no tiene detalles");
        List<LineaReservaInventarioRequest> lineas = detalles.stream().map(detalle -> {
            if (detalle.getIdAlmacen() == null) {
                throw new IllegalArgumentException("Todos los detalles deben indicar un almacén");
            }
            return new LineaReservaInventarioRequest(detalle.getIdAlmacen(), detalle.getIdProducto(), detalle.getCantidad());
        }).toList();

        inventoryServiceClient.reservar(new ReservaOrdenInventarioRequest(id, lineas,
                OffsetDateTime.now(ZoneOffset.UTC).plusHours(24)));
        try {
            cambiarEstado(pedido, EstadoPedido.CONFIRMED, actor, "Pedido confirmado y stock reservado");
            pedido.setConfirmadoEn(OffsetDateTime.now(ZoneOffset.UTC));
            return pedidoMapper.aResponse(pedidoRepository.save(pedido));
        } catch (RuntimeException ex) {
            inventoryServiceClient.liberar(id);
            throw ex;
        }
    }

    @Override
    @Transactional
    public PedidoResponse completar(UUID id, String actor) {
        Pedido pedido = bloquear(id);
        if (pedido.getEstado() == EstadoPedido.COMPLETED) return pedidoMapper.aResponse(pedido);
        if (pedido.getEstado() != EstadoPedido.CONFIRMED && pedido.getEstado() != EstadoPedido.PROCESSING) {
            throw new IllegalArgumentException("Solo se puede completar un pedido confirmado o en proceso");
        }
        inventoryServiceClient.consumir(id);
        cambiarEstado(pedido, EstadoPedido.COMPLETED, actor, "Pedido completado; inventario descontado");
        pedido.setCompletadoEn(OffsetDateTime.now(ZoneOffset.UTC));
        return pedidoMapper.aResponse(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional
    public PedidoResponse cancelar(UUID id, String actor, String motivo) {
        Pedido pedido = bloquear(id);
        if (pedido.getEstado() == EstadoPedido.CANCELLED) return pedidoMapper.aResponse(pedido);
        if (pedido.getEstado() == EstadoPedido.COMPLETED) {
            throw new IllegalArgumentException("Un pedido completado no puede cancelarse");
        }
        if (pedido.getEstado() == EstadoPedido.CONFIRMED || pedido.getEstado() == EstadoPedido.PROCESSING) {
            inventoryServiceClient.liberar(id);
        }
        cambiarEstado(pedido, EstadoPedido.CANCELLED, actor,
                motivo == null || motivo.isBlank() ? "Pedido cancelado" : motivo);
        pedido.setCanceladoEn(OffsetDateTime.now(ZoneOffset.UTC));
        return pedidoMapper.aResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public void eliminar(UUID id) {
        Pedido pedido = buscarEntidad(id);
        validarEditable(pedido);
        pedidoRepository.delete(pedido);
    }

    private Pedido buscarEntidad(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    private Pedido bloquear(UUID id) {
        return pedidoRepository.bloquearPorId(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    private void validarEditable(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.PENDING) {
            throw new IllegalArgumentException("Solo los pedidos pendientes pueden modificarse o eliminarse");
        }
    }

    private void cambiarEstado(Pedido pedido, EstadoPedido nuevo, String actor, String motivo) {
        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(pedido);
        historial.setEstadoAnterior(pedido.getEstado());
        historial.setNuevoEstado(nuevo);
        historial.setMotivo(motivo);
        historial.setCambiadoPor(actor);
        historialRepository.save(historial);
        pedido.setEstado(nuevo);
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
