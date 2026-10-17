package com.nexasupplyai.order.repository;

import com.nexasupplyai.order.entity.Pedido;
import com.nexasupplyai.order.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.LockModeType;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    List<Pedido> findAllByEstadoOrderBySolicitadoEnDesc(EstadoPedido estado);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select pedido from Pedido pedido where pedido.id = :id")
    Optional<Pedido> bloquearPorId(UUID id);
}
