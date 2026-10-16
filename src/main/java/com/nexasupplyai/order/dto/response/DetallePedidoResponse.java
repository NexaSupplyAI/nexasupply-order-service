package com.nexasupplyai.order.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record DetallePedidoResponse(
        UUID id,
        UUID idPedido,
        UUID idProducto,
        UUID idAlmacen,
        String skuInstantanea,
        String nombreProductoInstantanea,
        BigDecimal cantidad,
        BigDecimal precioUnitario,
        BigDecimal montoDescuento,
        BigDecimal totalLinea,
        OffsetDateTime creadoEn
) {
}
