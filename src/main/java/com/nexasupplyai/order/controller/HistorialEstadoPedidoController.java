package com.nexasupplyai.order.controller;

import com.nexasupplyai.order.dto.response.HistorialEstadoPedidoResponse;
import com.nexasupplyai.order.service.interfaces.HistorialEstadoPedidoServiceInterface;
import lombok.RequiredArgsConstructor;
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

}
