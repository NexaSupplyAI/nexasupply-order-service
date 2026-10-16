package com.nexasupplyai.order.client;

import com.nexasupplyai.order.dto.external.ProductoInventarioResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class InventoryServiceClient {

    private final RestClient clienteHttp;

    public InventoryServiceClient(
            RestClient.Builder constructorCliente,
            @Value("${clients.inventory-service.base-url:http://localhost:8081}") String urlBaseInventoryService
    ) {
        this.clienteHttp = constructorCliente.baseUrl(urlBaseInventoryService).build();
    }

    public ProductoInventarioResponse obtenerProducto(UUID idProducto) {
        return clienteHttp.get()
                .uri("/api/v1/productos/{id}", idProducto)
                .retrieve()
                .body(ProductoInventarioResponse.class);
    }
}
