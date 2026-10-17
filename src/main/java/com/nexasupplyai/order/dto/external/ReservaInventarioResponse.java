package com.nexasupplyai.order.dto.external;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservaInventarioResponse(
        UUID id,
        UUID idAlmacen,
        UUID idProducto,
        UUID idOrdenExterna,
        BigDecimal cantidad,
        String estado,
        OffsetDateTime expiraEn,
        OffsetDateTime creadoEn,
        OffsetDateTime actualizadoEn
) { }
