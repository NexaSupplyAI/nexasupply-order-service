package com.nexasupplyai.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "order_items",
        schema = "ordering",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_ordering_item_product",
                columnNames = {"order_id", "product_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class DetallePedido {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Pedido pedido;

    @Column(name = "product_id", nullable = false)
    private UUID idProducto;

    @Column(name = "warehouse_id")
    private UUID idAlmacen;

    @Column(name = "sku_snapshot", nullable = false, length = 80)
    private String skuInstantanea;

    @Column(name = "product_name_snapshot", nullable = false, length = 180)
    private String nombreProductoInstantanea;

    @Column(name = "quantity", nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "unit_price", nullable = false, precision = 14, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "discount_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal montoDescuento = BigDecimal.ZERO;

    @Column(name = "line_total", precision = 16, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalLinea;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime creadoEn;
}
