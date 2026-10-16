package com.nexasupplyai.order.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record DetallePedidoRequest(
        @NotNull UUID idPedido,
        @NotNull UUID idProducto,
        UUID idAlmacen,
        @NotBlank @Size(max = 80) String skuInstantanea,
        @NotBlank @Size(max = 180) String nombreProductoInstantanea,
        @NotNull @DecimalMin(value = "0.000", inclusive = false)
        @Digits(integer = 11, fraction = 3) BigDecimal cantidad,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal precioUnitario,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal montoDescuento
) {
}
