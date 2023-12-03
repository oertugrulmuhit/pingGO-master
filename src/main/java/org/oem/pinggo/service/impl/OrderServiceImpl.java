package org.oem.pinggo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.model.dtos.OrderDTO;
import org.oem.pinggo.model.entity.Order;
import org.oem.pinggo.model.entity.Product;
import org.oem.pinggo.model.entity.Seller;
import org.oem.pinggo.model.entity.User;
import org.oem.pinggo.model.enums.OrderStatus;
import org.oem.pinggo.exception.ItemOwnerException;
import org.oem.pinggo.exception.NoSuchElementFoundException;
import org.oem.pinggo.exception.OrderStatusNoEligibleForEditException;
import org.oem.pinggo.repository.OrderRepository;
import org.oem.pinggo.request.OrderRequest;
import org.oem.pinggo.service.OrderService;
import org.oem.pinggo.service.ProductService;
import org.oem.pinggo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final Translator translator;
    private final UserService userService;

    @Override
    public @NotNull OrderDTO getOrderById(long orderId) {
        final long currentUserId = userService.getCurrentUserId();
        log.info("{}  is getting  order with id {} ", currentUserId, orderId);
        final Order order = findById(orderId);
        return new OrderDTO(order);
    }

    @Override
    public @NotNull List<OrderDTO> getAll() {
        final User currentUser = userService.getCurrentUser();
        log.info("{}  is getting  all orders ", currentUser.getId());
        final List<Order> orderOfUser = orderRepository.findByCustomer(currentUser);
        final List<OrderDTO> orderDTOList = orderOfUser.stream().map(OrderDTO::new).toList();
        return orderDTOList;
    }

    @Override
    @Transactional
    public void accept(long orderId) {
        final Seller currentUser = userService.getCurrentSeller();
        log.info("{}  is accepting order with id {} ", currentUser.getId(), orderId);
        final Order order = validateOrderStatusIsCreated(orderId);
        productService.validateSellerIsOwnerOfProduct(currentUser.getId(), order.getProduct().getId());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setOrderStatusChangingTime(LocalDateTime.now());
        orderRepository.save(order);
        log.info("order with id {} is accepted by {}", orderId, currentUser.getId());
    }

    @Override
    @Transactional
    public void reject(long orderId) {
        final Seller currentUser = userService.getCurrentSeller();
        log.info("{}  is rejecting  order with id {} ", currentUser.getId(), orderId);
        final Order order = validateOrderStatusIsCreated(orderId);
        productService.validateSellerIsOwnerOfProduct(currentUser.getId(), order.getProduct().getId());
        order.setOrderStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
        productService.increaseQuantityForReturn(order.getProduct(), order.getQuantity());
        log.info("order with id {} is rejected by {}", orderId, currentUser.getId());
        log.info("product with id {}  is increased {} due to return ", order.getProduct().getId(), order.getQuantity());
    }

    @Override
    @Transactional
    public void deliverOrder() {
        final List<Order> allAccepted = findAllByOrderStatus(OrderStatus.ACCEPTED);
        final List<Order> willSave = new ArrayList<>();
        //15 dakikadan fazla zaman geçti
        allAccepted.stream().filter(o -> o.getOrderStatusChangingTime().plusMinutes(15).isBefore(LocalDateTime.now())).forEach(o -> {
            o.setOrderStatus(OrderStatus.DELIVERED);
            willSave.add(o);
        });
        allAccepted.stream().filter(o -> o.getOrderStatusChangingTime().plusMinutes(2).isBefore(LocalDateTime.now())).forEach(o -> {
            if (Math.random() < 0.5) {
                o.setOrderStatus(OrderStatus.DELIVERED);
                willSave.add(o);
            }
        });
        if (!willSave.isEmpty()) {
            orderRepository.saveAll(willSave);
            log.info("{} orders delivered successfully", willSave.size());
        }
    }

    @Override
    @Transactional
    public void cancel(long orderId) {
        final long currentUserId = userService.getCurrentUserId();
        log.info("{}  is cancelling order with id {} ", currentUserId, orderId);
        final Order order = validateOrderStatusIsCreated(orderId);
        validateUserIsOwnerOfOrder(currentUserId, orderId);
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        productService.increaseQuantityForReturn(order.getProduct(), order.getQuantity());
        log.info("order with id {} is cancelled by {}", orderId, currentUserId);
        log.info("product with id {}  is increased {} due to return ", order.getProduct().getId(), order.getQuantity());

    }

    @Override
    @Transactional
    public @NotNull OrderDTO create(@NotNull OrderRequest orderRequest) {
        final User currentUser = userService.getCurrentUser();
        log.info("{}  is creating  order with request: {} ", currentUser.getId(), orderRequest);
        final long productId = orderRequest.getProductID();
        final int orderQuantity = orderRequest.getQuantity();
        Product product = productService.decreaseQuantity(productId, orderQuantity);
        final Order newOrder = new Order();
        newOrder.setProduct(product);
        newOrder.setQuantity(orderQuantity);
        newOrder.setCustomer(currentUser);
        orderRepository.save(newOrder);
        log.info("{} created new order. Stock of  {} must be decrease {} ", currentUser.getId(), product.getId(), orderQuantity);
        return new OrderDTO(newOrder);
    }

    @Override
    public @NotNull List<Order> findByStatusAndStatusChangingTime(@NotNull OrderStatus orderStatus, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
        return orderRepository.findByOrderStatusAndOrderStatusChangingTimeBetween(orderStatus, startTime, endTime);
    }

    private @NotNull List<Order> findAllByOrderStatus(@NotNull OrderStatus orderStatus) {
        return orderRepository.findAllByOrderStatus(orderStatus);
    }

    private @NotNull Order validateOrderStatusIsCreated(long orderId) {
        final Order order = findById(orderId);
        final String message;
        boolean isStatusCreated = order.getOrderStatus().equals(OrderStatus.CREATED);
        if (!isStatusCreated) {
            message = translator.toLocale("only.newly.created.can.be.rejected.or.canceled.with.id.not.new", order.getId());
            log.error("{} status is not chanced : {}", order.getId(), message);
            throw new OrderStatusNoEligibleForEditException(message);
        }
        return order;
    }

    private void validateUserIsOwnerOfOrder(long userId, long orderId) {
        log.info("check :{} ordered order with id {} ", userId, orderId);
        boolean isExist = orderRepository.existsByOrderIDAndCustomerID(orderId, userId);
        if (!isExist) {
            final String errorMessage = translator.toLocale("with.id.order.is.from.different.user", orderId);
            log.error("{} user is trying to edit {} : {} ", userId, orderId, errorMessage);
            throw new ItemOwnerException(errorMessage);
        }
    }

    private @NotNull Order findById(long orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(() -> {
            final String errorMessage = translator.toLocale("order.not.found.exception.with.id", orderId);
            log.error("not found element : {}", errorMessage);
            return new NoSuchElementFoundException(errorMessage);
        });
        return order;
    }
}
