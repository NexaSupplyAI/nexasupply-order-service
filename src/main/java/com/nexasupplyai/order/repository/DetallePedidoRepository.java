package com.nexasupplyai.order.repository;

import com.nexasupplyai.order.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, UUID> {
    List<DetallePedido> findAllByPedidoId(UUID idPedido);
    Optional<DetallePedido> findByPedidoIdAndIdProducto(UUID idPedido, UUID idProducto);
}
