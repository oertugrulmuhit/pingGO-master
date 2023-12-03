package org.oem.pinggo.repository;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.entity.Order;
import org.oem.pinggo.model.entity.User;
import org.oem.pinggo.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @NotNull List<Order> findByCustomer(@NotNull User customer);

    @Query("select (count(o) > 0) from Order o where o.id = :oID and  o.customer.id = :cID")
    boolean existsByOrderIDAndCustomerID(@Param("oID") long oID, @Param("cID") long cID);

    List<Order> findByOrderStatusAndOrderStatusChangingTimeBetween(@NotNull OrderStatus orderStatus, @NotNull LocalDateTime start, @NotNull LocalDateTime end);

    @NotNull List<Order> findAllByOrderStatus(@NotNull OrderStatus orderStatus);

}