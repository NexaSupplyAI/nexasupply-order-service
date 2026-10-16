package com.nexasupplyai.order.dto.external;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Contrato mínimo consumido desde Inventory Service.
 * Debe mantenerse alineado con su endpoint de productos.
 */
public record ProductoInventarioResponse(
        UUID id,
        String sku,
        String nombre,
        BigDecimal precioVenta,
        Boolean activo
) {
}
