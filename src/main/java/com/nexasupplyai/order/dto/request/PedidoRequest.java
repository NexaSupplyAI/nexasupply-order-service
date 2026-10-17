package com.nexasupplyai.order.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record PedidoRequest(
        @NotBlank @Size(max = 40) String numeroPedido,
        UUID idClienteExterno,
        @Size(max = 180) String nombreCliente,
        @Email @Size(max = 180) String emailCliente,
        @Size(max = 50) String telefonoCliente,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String moneda,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal subtotal,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal montoImpuesto,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal montoDescuento,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal montoEnvio,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal montoTotal,
        String notas,
        @Size(max = 120) String creadoPor
) {
}
