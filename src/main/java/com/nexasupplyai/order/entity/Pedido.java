package com.nexasupplyai.order.entity;

import com.nexasupplyai.order.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders", schema = "ordering")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "order_number", nullable = false, unique = true, length = 40)
    private String numeroPedido;

    @Column(name = "external_customer_id")
    private UUID idClienteExterno;

    @Column(name = "customer_name", length = 180)
    private String nombreCliente;

    @Column(name = "customer_email", length = 180)
    private String emailCliente;

    @Column(name = "customer_phone", length = 50)
    private String telefonoCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private EstadoPedido estado = EstadoPedido.PENDING;

    @Column(name = "currency", nullable = false, length = 3)
    private String moneda = "PEN";

    @Column(name = "subtotal", nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal montoImpuesto = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal montoDescuento = BigDecimal.ZERO;

    @Column(name = "shipping_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal montoEnvio = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal montoTotal = BigDecimal.ZERO;

    @Column(name = "notes", columnDefinition = "text")
    private String notas;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false)
    private OffsetDateTime solicitadoEn;

    @Column(name = "confirmed_at")
    private OffsetDateTime confirmadoEn;

    @Column(name = "completed_at")
    private OffsetDateTime completadoEn;

    @Column(name = "cancelled_at")
    private OffsetDateTime canceladoEn;

    @Column(name = "created_by", length = 120)
    private String creadoPor;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime actualizadoEn;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("cambiadoEn DESC")
    private List<HistorialEstadoPedido> historialEstados = new ArrayList<>();
}
