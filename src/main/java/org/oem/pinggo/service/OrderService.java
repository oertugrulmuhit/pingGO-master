package org.oem.pinggo.service;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.dtos.OrderDTO;
import org.oem.pinggo.model.entity.Order;
import org.oem.pinggo.model.enums.OrderStatus;
import org.oem.pinggo.request.OrderRequest;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    @NotNull OrderDTO getOrderById(long orderId);

    @NotNull List<OrderDTO> getAll();

    @Transactional
    void accept(long orderId);

    @Transactional
    void deliverOrder();

    @Transactional
    void reject(long orderId);

    @Transactional
    void cancel(long orderId);

    @NotNull List<Order> findByStatusAndStatusChangingTime(@NonNull OrderStatus orderStatus, @NonNull LocalDateTime startTime,@NotNull LocalDateTime endTime);

    @NotNull OrderDTO create(OrderRequest orderRequest);
}
