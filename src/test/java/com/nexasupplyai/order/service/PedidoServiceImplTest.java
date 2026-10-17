package com.nexasupplyai.order.service;

import com.nexasupplyai.order.client.InventoryServiceClient;
import com.nexasupplyai.order.entity.DetallePedido;
import com.nexasupplyai.order.entity.Pedido;
import com.nexasupplyai.order.enums.EstadoPedido;
import com.nexasupplyai.order.mapper.PedidoMapper;
import com.nexasupplyai.order.repository.*;
import com.nexasupplyai.order.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {
    @Mock PedidoRepository pedidos;
    @Mock PedidoMapper mapper;
    @Mock DetallePedidoRepository detalles;
    @Mock HistorialEstadoPedidoRepository historial;
    @Mock InventoryServiceClient inventario;

    @Test
    void confirmarReservaInventarioYRegistraHistorial() {
        UUID id = UUID.randomUUID();
        Pedido pedido = new Pedido(); pedido.setId(id); pedido.setEstado(EstadoPedido.PENDING);
        DetallePedido detalle = new DetallePedido(); detalle.setPedido(pedido); detalle.setIdAlmacen(UUID.randomUUID());
        detalle.setIdProducto(UUID.randomUUID()); detalle.setCantidad(new BigDecimal("2"));
        when(pedidos.bloquearPorId(id)).thenReturn(Optional.of(pedido));
        when(detalles.findAllByPedidoId(id)).thenReturn(List.of(detalle));
        when(pedidos.save(any())).thenAnswer(i -> i.getArgument(0));
        var servicio = new PedidoServiceImpl(pedidos, mapper, detalles, historial, inventario);

        servicio.confirmar(id, "usuario-prueba");

        assertEquals(EstadoPedido.CONFIRMED, pedido.getEstado());
        verify(inventario).reservar(any());
        verify(historial).save(any());
    }
}
