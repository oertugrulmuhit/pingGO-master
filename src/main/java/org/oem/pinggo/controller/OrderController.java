package org.oem.pinggo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.model.dtos.OrderDTO;
import org.oem.pinggo.request.OrderRequest;
import org.oem.pinggo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@Parameter(description = "id of order you want to inspect", example = "1") @PathVariable long orderId) {
        final OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "User's orders", description = "gets logged user's orders")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAll() {
        final List<OrderDTO> orderDTOS = orderService.getAll();
        return ResponseEntity.ok(orderDTOS);

    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody OrderRequest orderRequest) {
        final OrderDTO created = orderService.create(orderRequest);
        return ResponseEntity.ok(created);
    }


    @DeleteMapping("/{orderId}/reject")
    public ResponseEntity<?> reject(@Parameter(description = "id of order you want to reject", example = "1") @PathVariable long orderId) {
        orderService.reject(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PutMapping("/{orderId}/accept")
    public ResponseEntity<?> accept(@Parameter(description = "id of order you want to accept", example = "1") @PathVariable long orderId) {
        orderService.accept(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancel(@Parameter(description = "id of order you want to cancel", example = "1") @PathVariable long orderId) {
        orderService.cancel(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
