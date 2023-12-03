package org.oem.pinggo.model.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.oem.pinggo.model.entity.Order;
import org.oem.pinggo.model.enums.OrderStatus;

import java.time.LocalDateTime;

/**
 * DTO for {@link Order}
 */
@Data
public class OrderDTO {

    private int quantity;

    private OrderStatus orderStatus;

    private long customerId;

    private long productId;

    @NotNull
    private LocalDateTime orderStatusChangingTime;


    public OrderDTO(Order order) {
        this.quantity = order.getQuantity();
        this.orderStatus = order.getOrderStatus();
        this.customerId = order.getCustomer().getId();
        this.productId = order.getProduct().getId();
        this.orderStatusChangingTime = order.getOrderStatusChangingTime();
    }
}