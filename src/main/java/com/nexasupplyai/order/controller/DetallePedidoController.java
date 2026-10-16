package com.nexasupplyai.order.controller;

import com.nexasupplyai.order.dto.request.DetallePedidoRequest;
import com.nexasupplyai.order.dto.response.DetallePedidoResponse;
import com.nexasupplyai.order.service.interfaces.DetallePedidoServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/detalles-pedido")
@RequiredArgsConstructor
public class DetallePedidoController {
    private final DetallePedidoServiceInterface detallePedidoService;

    @GetMapping
    public List<DetallePedidoResponse> listarPorPedido(@RequestParam UUID idPedido) {
        return detallePedidoService.listarPorPedido(idPedido);
    }

    @GetMapping("/{id}")
    public DetallePedidoResponse obtenerPorId(@PathVariable UUID id) {
        return detallePedidoService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<DetallePedidoResponse> crear(@Valid @RequestBody DetallePedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detallePedidoService.crear(request));
    }

    @PutMapping("/{id}")
    public DetallePedidoResponse actualizar(@PathVariable UUID id, @Valid @RequestBody DetallePedidoRequest request) {
        return detallePedidoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        detallePedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
