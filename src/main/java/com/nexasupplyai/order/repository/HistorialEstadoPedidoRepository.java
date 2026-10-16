package com.nexasupplyai.order.repository;

import com.nexasupplyai.order.entity.HistorialEstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistorialEstadoPedidoRepository extends JpaRepository<HistorialEstadoPedido, UUID> {
    List<HistorialEstadoPedido> findAllByPedidoIdOrderByCambiadoEnDesc(UUID idPedido);
}
