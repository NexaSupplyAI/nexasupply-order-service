package com.nexasupplyai.order.controller;

import com.nexasupplyai.order.dto.request.HistorialEstadoPedidoRequest;
import com.nexasupplyai.order.dto.response.HistorialEstadoPedidoResponse;
import com.nexasupplyai.order.service.interfaces.HistorialEstadoPedidoServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/historial-estados-pedido")
@RequiredArgsConstructor
public class HistorialEstadoPedidoController {
    private final HistorialEstadoPedidoServiceInterface historialEstadoPedidoService;

    @GetMapping
    public List<HistorialEstadoPedidoResponse> listarPorPedido(@RequestParam UUID idPedido) {
        return historialEstadoPedidoService.listarPorPedido(idPedido);
    }

    @GetMapping("/{id}")
    public HistorialEstadoPedidoResponse obtenerPorId(@PathVariable UUID id) {
        return historialEstadoPedidoService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<HistorialEstadoPedidoResponse> crear(
            @Valid @RequestBody HistorialEstadoPedidoRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historialEstadoPedidoService.crear(request));
    }

    @PutMapping("/{id}")
    public HistorialEstadoPedidoResponse actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody HistorialEstadoPedidoRequest request
    ) {
        return historialEstadoPedidoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        historialEstadoPedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
