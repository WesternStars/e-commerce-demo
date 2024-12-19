package com.bymdev.artem.ecommercedemo.service;

import com.bymdev.artem.ecommercedemo.entity.OrderItem;
import com.bymdev.artem.ecommercedemo.entity.Product;
import com.bymdev.artem.ecommercedemo.repository.OrderItemRepository;
import com.bymdev.artem.ecommercedemo.repository.ProductRepository;
import com.bymdev.artem.ecommercedemo.request.OrderItemRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderItem getOrderItem(int id) {
        return orderItemRepository.findById(id).orElseThrow();
    }

    public List<OrderItem> getOrderItems(int count, int page) {
        Iterable<OrderItem> all = orderItemRepository.findAll(PageRequest.of(page, count));
        List<OrderItem> orderItems = new ArrayList<>();
        all.forEach(orderItems::add);
        return orderItems;
    }

    public void createOrderItem(OrderItemRequest request) {
        Product product = productRepository.findById(request.productSku()).orElseThrow();
        orderItemRepository.save(new OrderItem(null, request.quantity(), product, null));
    }

    public void updateOrderItem(int id, OrderItemRequest request) {
        Product product = productRepository.findById(request.productSku()).orElseThrow();
        orderItemRepository.save(new OrderItem(id, request.quantity(), product, null));
    }
}
