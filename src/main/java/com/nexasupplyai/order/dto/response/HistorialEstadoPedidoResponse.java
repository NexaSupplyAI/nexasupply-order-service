package com.nexasupplyai.order.dto.response;

import com.nexasupplyai.order.enums.EstadoPedido;

import java.time.OffsetDateTime;
import java.util.UUID;

public record HistorialEstadoPedidoResponse(
        UUID id,
        UUID idPedido,
        EstadoPedido estadoAnterior,
        EstadoPedido nuevoEstado,
        String motivo,
        String cambiadoPor,
        OffsetDateTime cambiadoEn
) {
}
