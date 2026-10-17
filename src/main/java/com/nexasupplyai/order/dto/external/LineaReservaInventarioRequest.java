package com.nexasupplyai.order.dto.external;

import java.math.BigDecimal;
import java.util.UUID;

public record LineaReservaInventarioRequest(UUID idAlmacen, UUID idProducto, BigDecimal cantidad) {
}
