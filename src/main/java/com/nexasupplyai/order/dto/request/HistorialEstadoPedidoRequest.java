package com.nexasupplyai.order.dto.request;

import com.nexasupplyai.order.enums.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public record HistorialEstadoPedidoRequest(
        @NotNull UUID idPedido,
        EstadoPedido estadoAnterior,
        @NotNull EstadoPedido nuevoEstado,
        @Size(max = 300) String motivo,
        @Size(max = 120) String cambiadoPor,
        OffsetDateTime cambiadoEn
) {
}
