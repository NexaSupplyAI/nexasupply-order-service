package com.nexasupplyai.order.entity;

import com.nexasupplyai.order.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_status_history", schema = "ordering")
@Getter
@Setter
@NoArgsConstructor
public class HistorialEstadoPedido {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 30)
    private EstadoPedido estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private EstadoPedido nuevoEstado;

    @Column(name = "reason", length = 300)
    private String motivo;

    @Column(name = "changed_by", length = 120)
    private String cambiadoPor;

    @CreationTimestamp
    @Column(name = "changed_at", nullable = false)
    private OffsetDateTime cambiadoEn;
}
