package com.nexasupplyai.order.dto.external;

import java.util.List;
import java.util.UUID;

public record OperacionInventarioResponse(UUID idOrden, String resultado, boolean idempotente,
                                          List<ReservaInventarioResponse> reservas) {
}
