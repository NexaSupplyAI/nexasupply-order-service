package com.nexasupplyai.order.controller;

import com.nexasupplyai.order.dto.request.PedidoRequest;
import com.nexasupplyai.order.dto.response.PedidoResponse;
import com.nexasupplyai.order.enums.EstadoPedido;
import com.nexasupplyai.order.service.interfaces.PedidoServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoServiceInterface pedidoService;

    @GetMapping
    public List<PedidoResponse> listar(@RequestParam(required = false) EstadoPedido estado) {
        return pedidoService.listar(estado);
    }

    @GetMapping("/{id}")
    public PedidoResponse obtenerPorId(@PathVariable UUID id) {
        return pedidoService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(request));
    }

    @PutMapping("/{id}")
    public PedidoResponse actualizar(@PathVariable UUID id, @Valid @RequestBody PedidoRequest request) {
        return pedidoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
