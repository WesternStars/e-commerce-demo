package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.Order;
import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import com.bymdev.artem.ecommercedemo.request.OrderItemRequest;
import com.bymdev.artem.ecommercedemo.response.OrderItemResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderItemResponse getOrderItem(int id) {
        OrderItem item = orderItemRepository.findById(id).orElseThrow();
        return new OrderItemResponse(
                item.getId(),
                item.getQuantity(),
                item.getProduct().getSku(),
                Optional.ofNullable(item.getOrder()).map(Order::getId).orElse(0)
        );
    }

    public List<OrderItemResponse> getOrderItems(int count, int page) {
        return orderItemRepository.findAll(PageRequest.of(page, count))
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getQuantity(),
                        item.getProduct().getSku(),
                        Optional.ofNullable(item.getOrder()).map(Order::getId).orElse(0)
                ))
                .toList();
    }

    public OrderItemResponse createOrderItem(OrderItemRequest request) {
        Product product = productRepository.findById(request.productSku()).orElseThrow();
        OrderItem item = orderItemRepository.save(new OrderItem(null, request.quantity(), product, null));
        return new OrderItemResponse(
                item.getId(),
                item.getQuantity(),
                item.getProduct().getSku(),
                Optional.ofNullable(item.getOrder()).map(Order::getId).orElse(0)
        );
    }

    public OrderItemResponse updateOrderItem(int id, OrderItemRequest request) {
        Product product = productRepository.findById(request.productSku()).orElseThrow();
        OrderItem item = orderItemRepository.save(new OrderItem(id, request.quantity(), product, null));
        return new OrderItemResponse(
                item.getId(),
                item.getQuantity(),
                item.getProduct().getSku(),
                Optional.ofNullable(item.getOrder()).map(Order::getId).orElse(0)
        );
    }

    public void delete(int id) {
        orderItemRepository.deleteById(id);
    }
}
