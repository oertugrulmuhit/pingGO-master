package org.oem.pinggo.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.oem.pinggo.model.enums.OrderStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Column(name = "order_status_changing_time", nullable = false)
    private LocalDateTime orderStatusChangingTime = LocalDateTime.now();
}