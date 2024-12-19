package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.entity.Order;
import com.bymdev.artem.ecommercedemo.request.OrderRequest;
import com.bymdev.artem.ecommercedemo.service.OrderService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    OrderService orderService;

    @GetMapping("/")
    public List<Order> getOrders(
            @Min(value = 1, message = "The count must be greater than 0") @RequestParam(value = "count", defaultValue = "100") int count,
            @Min(value = 0, message = "The page started from 0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return orderService.getOrders(count, page);
    }

    @GetMapping("/{id}")
    public Order getOrder(@Positive(message = "The order id must be greater than 0") @PathVariable Integer id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/")
    @ResponseStatus(CREATED)
    public void createOrder(@Validated @RequestBody OrderRequest request) {
        orderService.createOrder(request);
    }

    @PutMapping("/{id}")
    public void updateOrder(@Positive(message = "The order id must be greater than 0") @PathVariable Integer id,
                            @Validated @RequestBody OrderRequest request) {
        orderService.updateOrder(id, request);
    }
}
