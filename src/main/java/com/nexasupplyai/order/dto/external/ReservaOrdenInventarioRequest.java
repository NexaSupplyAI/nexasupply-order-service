package com.nexasupplyai.order.dto.external;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ReservaOrdenInventarioRequest(UUID idOrden, List<LineaReservaInventarioRequest> lineas,
                                            OffsetDateTime expiraEn) {
}
