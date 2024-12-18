package com.bymdev.artem.ecommercedemo.controller;

import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.request.OrderItemRequest;
import com.bymdev.artem.ecommercedemo.service.OrderItemService;
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
@RequestMapping("/api/order-item")
public class OrderItemController {

    OrderItemService orderItemService;

    @GetMapping("/")
    public List<OrderItem> getOrderItems(
            @Min(value = 1, message = "The count must be greater than 0") @RequestParam(value = "count", defaultValue = "100") int count,
            @Min(value = 0, message = "The page started from 0") @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return orderItemService.getOrderItems(count, page);
    }

    @GetMapping("/{id}")
    public OrderItem getOrderItem(@Positive(message = "The orderItem id must be greater than 0") @PathVariable Integer id) {
        return orderItemService.getOrderItem(id);
    }

    @PostMapping("/")
    @ResponseStatus(CREATED)
    public void createOrderItem(@Validated @RequestBody OrderItemRequest request) {
        orderItemService.createOrderItem(request);
    }

    @PutMapping("/{id}")
    public void updateOrderItem(
            @Positive(message = "The orderItem id must be greater than 0") @PathVariable Integer id,
            @Validated @RequestBody OrderItemRequest request
    ) {
        orderItemService.updateOrderItem(id, request);
    }
}
